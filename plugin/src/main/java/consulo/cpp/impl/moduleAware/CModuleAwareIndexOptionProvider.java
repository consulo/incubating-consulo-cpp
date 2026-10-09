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
package consulo.cpp.impl.moduleAware;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.impl.module.extension.CModuleExtension;
import consulo.cpp.lang.CHeaderFileType;
import consulo.cpp.lang.CPPHeaderFileType;
import consulo.cpp.lang.CPPSourceFileType;
import consulo.cpp.lang.CSourceFileType;
import consulo.cpp.moduleAware.CDefineEnv;
import consulo.cpp.moduleAware.CIncludeWalker;
import consulo.cpp.moduleAware.CPreprocessorOptions;
import consulo.cpp.moduleAware.CPreprocessorOptionsExternalizer;
import consulo.language.psi.stub.IndexOption;
import consulo.language.psi.stub.ModuleAwareIndexOptionProvider;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Tells the module-aware index that a C/C++ file's indexed data depends on the defines of its module: modules
 * with the same defines share one copy, modules with different defines get their own.
 */
@ExtensionImpl
public class CModuleAwareIndexOptionProvider implements ModuleAwareIndexOptionProvider {
    private static final Set<FileType> FILE_TYPES = Set.of(
        CSourceFileType.INSTANCE,
        CPPSourceFileType.INSTANCE,
        CHeaderFileType.INSTANCE,
        CPPHeaderFileType.INSTANCE
    );

    @Override
    public String getId() {
        return CDefineEnv.PROVIDER_ID;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public Set<FileType> getInputFileTypes() {
        return FILE_TYPES;
    }

    private static List<VirtualFile> resolveIncludeRoots(List<String> includePaths) {
        List<VirtualFile> roots = new ArrayList<>();
        for (String path : includePaths) {
            VirtualFile root = LocalFileSystem.getInstance().findFileByPath(path);
            if (root != null && root.isDirectory()) {
                roots.add(root);
            }
        }
        return roots;
    }

    @Override
    public IndexOption getOptions(Module module, VirtualFile file) {
        CModuleExtension extension = module.getExtension(CModuleExtension.class);
        if (extension == null) {
            return IndexOption.fullySharable();
        }

        List<VirtualFile> includeRoots = resolveIncludeRoots(extension.getIncludePaths());
        CPreprocessorOptions options = new CPreprocessorOptions(
            extension.getDefines(),
            extension.getIncludePaths(),
            CIncludeWalker.fingerprintFor(file, includeRoots)
        );
        if (options.isEmpty()) {
            return IndexOption.fullySharable();
        }
        return IndexOption.sharablePerOption(
            options,
            CPreprocessorOptionsExternalizer.INSTANCE,
            LocalizeValue.of(CModuleExtension.formatDefines(options.defines()))
        );
    }
}
