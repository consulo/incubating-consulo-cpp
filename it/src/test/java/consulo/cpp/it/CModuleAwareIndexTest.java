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
import org.napile.cpp4idea.lang.psi.CPsiField;
import org.napile.cpp4idea.CLanguage;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiFile;
import consulo.application.ReadAction;
import consulo.cpp.impl.module.extension.CMutableModuleExtension;
import consulo.cpp.moduleAware.CDefineEnv;
import consulo.cpp.moduleAware.CPreprocessorOptions;
import consulo.it.AllowLogError;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;
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

            CMutableModuleExtension extension = rootModel.getExtensionWithoutCheck(CMutableModuleExtension.class);
            assertNotNull(extension, "the C/C++ module extension must be registered");
            extension.setEnabled(true);
            extension.setDefines(defines);

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
