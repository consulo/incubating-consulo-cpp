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

package consulo.cpp;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionUtil;
import consulo.test.junit.impl.language.SimpleParsingTest;
import consulo.virtualFileSystem.fileType.FileType;
import jakarta.annotation.Nullable;
import org.napile.cpp4idea.CLanguage;

/**
 * Base class for C/C++ parsing tests.
 * Concrete subclasses provide the file type and data path.
 */
public abstract class CParsingBaseTest extends SimpleParsingTest<Object> {
    public CParsingBaseTest(String dataPath, String extension) {
        super(dataPath, extension);
    }

    /**
     * Only check the primary C/C++ PSI root, not the preprocessor root.
     */
    @Override
    protected boolean checkAllPsiRoots() {
        return false;
    }

    @RequiredReadAction
    @Override
    protected LanguageVersion resolveLanguageVersion(Context context, @Nullable Object testContext, FileType fileType) {
        return LanguageVersionUtil.findDefaultVersion(CLanguage.INSTANCE);
    }
}
