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
package consulo.cpp.preprocessor;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.CLanguage;
import consulo.cpp.lang.version.CLanguageVersion;
import consulo.cpp.lang.version.CLanguageVersions;
import consulo.language.Language;
import consulo.language.psi.PsiElement;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionResolver;
import consulo.language.version.LanguageVersionUtil;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

/**
 * The preprocessor layer of a file is parsed with the {@link CLanguageVersion#getPreprocessorVersion() preprocessor
 * version} of the file's C version.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
@ExtensionImpl
public class CPreprocessorLanguageVersionResolver implements LanguageVersionResolver {
    @RequiredReadAction
    @Override
    public LanguageVersion getLanguageVersion(Language language, @Nullable PsiElement element) {
        CLanguageVersion cLanguageVersion = element == null ? CLanguageVersions.getDefaultCpp() : CLanguageVersions.of(element);
        return cLanguageVersion.getPreprocessorVersion();
    }

    @RequiredReadAction
    @Override
    public LanguageVersion getLanguageVersion(Language language, @Nullable Project project, @Nullable VirtualFile virtualFile) {
        LanguageVersion version = LanguageVersionUtil.findLanguageVersion(CLanguage.INSTANCE, project, virtualFile);
        CLanguageVersion cLanguageVersion = version instanceof CLanguageVersion cVersion ? cVersion : CLanguageVersions.getDefaultCpp();
        return cLanguageVersion.getPreprocessorVersion();
    }

    @Override
    public Language getLanguage() {
        return CPreprocessorLanguage.INSTANCE;
    }
}
