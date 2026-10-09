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
package consulo.cpp.moduleAware;

import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.ModuleAwareIndexOptions;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Reads back the preprocessor options the module-aware index recorded for a file, so parsing and highlighting
 * see exactly what the file was indexed under.
 */
public final class CDefineEnv {
    public static final String PROVIDER_ID = "cpp-module-aware";

    private CDefineEnv() {
    }

    public static Map<String, String> seedFor(PsiElement context) {
        return optionsFor(context).defines();
    }

    public static Map<String, String> seedFor(Project project, VirtualFile file) {
        return optionsFor(project, file).defines();
    }

    public static CPreprocessorOptions optionsFor(PsiElement context) {
        return read(() -> ModuleAwareIndexOptions.getOptions(context, PROVIDER_ID, CPreprocessorOptionsExternalizer.INSTANCE));
    }

    public static CPreprocessorOptions optionsFor(Project project, VirtualFile file) {
        return read(() -> ModuleAwareIndexOptions.getOptions(project, file, PROVIDER_ID, CPreprocessorOptionsExternalizer.INSTANCE));
    }

    /**
     * Resolves the recorded include directories; entries which no longer exist are skipped rather than failing
     * the parse.
     */
    public static List<VirtualFile> includeRootsFor(PsiElement context) {
        List<VirtualFile> roots = new ArrayList<>();
        for (String path : optionsFor(context).includePaths()) {
            VirtualFile root = LocalFileSystem.getInstance().findFileByPath(path);
            if (root != null && root.isDirectory()) {
                roots.add(root);
            }
        }
        return roots;
    }

    /**
     * The module-aware index is a service of a full application; a light fixture which only parses a file has no
     * such service, and there parsing simply proceeds with no options rather than failing.
     */
    private static CPreprocessorOptions read(Supplier<CPreprocessorOptions> reader) {
        CPreprocessorOptions options;
        try {
            options = reader.get();
        }
        catch (IllegalArgumentException serviceNotBound) {
            return CPreprocessorOptions.EMPTY;
        }
        return options == null ? CPreprocessorOptions.EMPTY : options;
    }
}
