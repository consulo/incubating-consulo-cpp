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
package consulo.cpp.preprocessor.psi.impl;

import consulo.cpp.moduleAware.CDefineEnv;
import consulo.cpp.preprocessor.psi.CPsiSharpInclude;
import consulo.cpp.preprocessor.psi.CPsiSharpIndepInclude;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiReferenceBase;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.Nullable;

/**
 * Resolves {@code #include "name"} relative to the directory of the file holding the directive, then through the
 * module include paths; {@code #include <name>} only through the include paths - the same order
 * {@code PreprocessorExpander} uses when it expands the include.
 */
public class CIncludeReference extends PsiReferenceBase<CPsiSharpInclude> {
    public CIncludeReference(CPsiSharpInclude element, TextRange rangeInElement) {
        super(element, rangeInElement);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        String includeName = getElement().getIncludeName();
        if (includeName == null || includeName.isEmpty()) {
            return null;
        }

        PsiFile containingFile = getElement().getContainingFile();
        VirtualFile currentFile = containingFile == null ? null : containingFile.getOriginalFile().getVirtualFile();
        if (currentFile == null) {
            return null;
        }

        VirtualFile target = null;
        if (!(getElement() instanceof CPsiSharpIndepInclude)) {
            VirtualFile parent = currentFile.getParent();
            target = parent == null ? null : findFile(parent, includeName);
        }
        if (target == null) {
            for (VirtualFile root : CDefineEnv.includeRootsFor(getElement())) {
                target = findFile(root, includeName);
                if (target != null) {
                    break;
                }
            }
        }
        return target == null ? null : PsiManager.getInstance(getElement().getProject()).findFile(target);
    }

    @Nullable
    private static VirtualFile findFile(VirtualFile directory, String includeName) {
        VirtualFile file = directory.findFileByRelativePath(includeName);
        return file == null || file.isDirectory() ? null : file;
    }

    @Override
    public Object[] getVariants() {
        return EMPTY_ARRAY;
    }
}
