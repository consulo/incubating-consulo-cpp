/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.cpp.it;

import consulo.application.Application;
import consulo.application.WriteAction;
import consulo.cpp.lang.psi.CPsiField;
import consulo.cpp.lang.CLanguage;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiFile;
import consulo.application.ReadAction;
import consulo.cpp.module.extension.CDefaultMutableModuleExtension;
import consulo.cpp.moduleAware.CDefineEnv;
import consulo.cpp.moduleAware.CPreprocessorOptions;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.CPsiSharpInclude;
import consulo.it.AllowLogError;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.it.HeadlessApplicationExtension;
import consulo.module.ModifiableModuleModel;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.project.DumbService;
import consulo.project.Project;
import consulo.project.ProjectManager;
import consulo.project.ProjectOpenContext;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Drives the module-aware index against a real project: a module carries C/C++ defines, and the index has to
 * record them for the files of that module so parsing and highlighting can read back the options a file was
 * indexed under. The lighter tests in {@code cpp-api} cover the pieces; this one covers them wired together.
 */
@ExtendWith(HeadlessApplicationExtension.class)
@AllowLogError({
    "consulo.virtualFileSystem.internal.BaseVirtualFileManager",
    "consulo.application.impl.internal.BaseApplication",
    "consulo.ui.ex.impl.internal.action.ActionManagerImpl"
})
public class CModuleAwareIndexTest {
    private static final long TIMEOUT_SECONDS = 60;

    @Test
    public void moduleDefinesAreRecordedForItsFiles(Application application, ProjectManager projectManager) throws Exception {
        Path directory = Files.createTempDirectory("consulo-cpp-it-defines");
        Path source = directory.resolve("main.c");
        Files.writeString(source, "#ifdef FEATURE\nint enabled;\n#else\nint disabled;\n#endif\n");

        Project project = openProjectWithModule(application, projectManager, directory, Map.of("FEATURE", "1"));

        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(source);
        assertNotNull(file, "the source file must be visible in the VFS");

        waitFor("the module defines must reach the index", () -> {
            CPreprocessorOptions options = CDefineEnv.optionsFor(project, file);
            return options.defines().containsKey("FEATURE");
        });

        CPreprocessorOptions options = CDefineEnv.optionsFor(project, file);
        assertEquals("1", options.defines().get("FEATURE"));
    }

    /**
     * A module with nothing configured must not force a private copy of the indexed data, otherwise every plain
     * C file in a project would stop sharing its index entries.
     */
    @Test
    public void moduleWithoutDefinesRecordsNothing(Application application, ProjectManager projectManager) throws Exception {
        Path directory = Files.createTempDirectory("consulo-cpp-it-plain");
        Path source = directory.resolve("plain.c");
        Files.writeString(source, "int value;\n");

        Project project = openProjectWithModule(application, projectManager, directory, Map.of());

        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(source);
        assertNotNull(file);

        awaitSmart(project);
        assertTrue(CDefineEnv.optionsFor(project, file).defines().isEmpty());
    }

    /**
     * The point of the whole module-aware chain: the defines configured on the module decide which branch of a
     * conditional is actually compiled, so a file in a module that defines {@code FEATURE} parses the guarded
     * declaration and not the {@code #else} one.
     */
    @Test
    public void definesDecideWhichBranchIsParsed(Application application, ProjectManager projectManager) throws Exception {
        Path directory = Files.createTempDirectory("consulo-cpp-it-branch-on");
        Path source = directory.resolve("main.c");
        Files.writeString(source, "#ifdef FEATURE\nint enabled;\n#else\nint disabled;\n#endif\n");

        Project project = openProjectWithModule(application, projectManager, directory, Map.of("FEATURE", ""));
        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(source);
        assertNotNull(file);

        waitFor("the guarded declaration must be the one parsed", () -> declarationsOf(project, file).contains("enabled"));
        assertFalse(declarationsOf(project, file).contains("disabled"), "the #else declaration must not be parsed");
    }

    /**
     * The same file in a module without that define compiles the other branch instead.
     */
    @Test
    public void withoutTheDefineTheElseBranchIsParsed(Application application, ProjectManager projectManager) throws Exception {
        Path directory = Files.createTempDirectory("consulo-cpp-it-branch-off");
        Path source = directory.resolve("main.c");
        Files.writeString(source, "#ifdef FEATURE\nint enabled;\n#else\nint disabled;\n#endif\n");

        Project project = openProjectWithModule(application, projectManager, directory, Map.of());
        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(source);
        assertNotNull(file);

        waitFor("the #else declaration must be the one parsed", () -> declarationsOf(project, file).contains("disabled"));
        assertFalse(declarationsOf(project, file).contains("enabled"), "the guarded declaration must not be parsed");
    }

    /**
     * What a CMake import sets up: the module searches a directory outside its content - as the compiler searches
     * {@code /usr/include} - so {@code #include <...>} resolves there, and the defines of the header it finds decide
     * which branch is parsed.
     */
    @Test
    public void includePathOutsideTheProjectIsSearched(Application application, ProjectManager projectManager) throws Exception {
        Path includeDirectory = Files.createTempDirectory("consulo-cpp-it-system-include");
        Path header = includeDirectory.resolve("config.h");
        Files.writeString(header, "#define FEATURE 1\n");

        Path directory = Files.createTempDirectory("consulo-cpp-it-include");
        Path source = directory.resolve("main.c");
        Files.writeString(source, "#include <config.h>\n#ifdef FEATURE\nint enabled;\n#else\nint disabled;\n#endif\n");

        Project project = openProjectWithModule(application, projectManager, directory, Map.of(), List.of(includeDirectory.toString()));
        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(source);
        VirtualFile headerFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(header);
        assertNotNull(file);
        assertNotNull(headerFile);

        waitFor("the include must resolve through the module include path", () -> headerFile.equals(includeTargetOf(project, file)));
        waitFor("the define of the included header must decide the branch", () -> declarationsOf(project, file).contains("enabled"));
        assertFalse(declarationsOf(project, file).contains("disabled"), "the #else declaration must not be parsed");
    }

    /**
     * @return the file the first {@code #include} of {@code file} resolves to
     */
    private static @Nullable VirtualFile includeTargetOf(Project project, VirtualFile file) {
        return ReadAction.compute(() -> {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            PsiFile preprocessorRoot = psiFile == null ? null : psiFile.getViewProvider().getPsi(CPreprocessorLanguage.INSTANCE);
            CPsiSharpInclude include = preprocessorRoot == null ? null : PsiTreeUtil.findChildOfType(preprocessorRoot, CPsiSharpInclude.class);
            PsiReference reference = include == null ? null : include.getReference();
            PsiElement target = reference == null ? null : reference.resolve();
            return target instanceof PsiFile targetFile ? targetFile.getVirtualFile() : null;
        });
    }

    /**
     * @return the text of every declaration the C parse actually produced, which is how the active branch is
     * observed - the inactive branch survives only as dropped text around it, never as a declaration
     */
    private static String declarationsOf(Project project, VirtualFile file) {
        return ReadAction.compute(() -> {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile == null) {
                return "";
            }
            PsiFile cRoot = (PsiFile)psiFile.getViewProvider().getPsi(CLanguage.INSTANCE);
            if (cRoot == null) {
                return "";
            }
            StringBuilder declarations = new StringBuilder();
            for (CPsiField field : PsiTreeUtil.findChildrenOfType(cRoot, CPsiField.class)) {
                declarations.append(field.getText()).append('\n');
            }
            return declarations.toString();
        });
    }

    private static Project openProjectWithModule(Application application,
                                                 ProjectManager projectManager,
                                                 Path directory,
                                                 Map<String, String> defines) throws Exception {
        return openProjectWithModule(application, projectManager, directory, defines, List.of());
    }

    private static Project openProjectWithModule(Application application,
                                                 ProjectManager projectManager,
                                                 Path directory,
                                                 Map<String, String> defines,
                                                 List<String> includePaths) throws Exception {
        Project project = projectManager
            .openProjectAsync(directory, application.getLastUIAccess(), new ProjectOpenContext())
            .get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(project);

        VirtualFile directoryFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(directory);
        assertNotNull(directoryFile);

        ModuleManager moduleManager = ModuleManager.getInstance(project);
        WriteAction.run(() -> {
            ModifiableModuleModel moduleModel = moduleManager.getModifiableModel();
            Module module = moduleModel.newModule("main", directory.toString());
            moduleModel.commit();

            ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
            rootModel.addContentEntry(directoryFile);

            CDefaultMutableModuleExtension extension = rootModel.getExtensionWithoutCheck(CDefaultMutableModuleExtension.class);
            assertNotNull(extension, "the C/C++ module extension must be registered");
            extension.setEnabled(true);
            extension.setDefines(defines);
            extension.setIncludePaths(includePaths);

            rootModel.commit();
        });

        awaitSmart(project);
        return project;
    }

    private static void awaitSmart(Project project) throws Exception {
        CountDownLatch smart = new CountDownLatch(1);
        DumbService.getInstance(project).runWhenSmart(smart::countDown);
        assertTrue(smart.await(TIMEOUT_SECONDS, TimeUnit.SECONDS), "project must reach smart mode");
    }

    private static void waitFor(String description, BooleanSupplier condition) throws Exception {
        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS);
        while (System.currentTimeMillis() < deadline) {
            if (condition.getAsBoolean()) {
                return;
            }
            Thread.sleep(50);
        }
        assertTrue(condition.getAsBoolean(), "timed out: " + description);
    }
}
