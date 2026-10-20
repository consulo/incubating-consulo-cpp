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
package consulo.cpp.lang.version;

import consulo.annotation.component.ExtensionImpl;
import jakarta.inject.Inject;

/**
 * ISO C++11.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
@ExtensionImpl
public class Cpp11LanguageVersion extends Cpp98LanguageVersion {
    public static final String ID = "CPP11";

    @Inject
    public Cpp11LanguageVersion() {
        this(ID, "C++11");
    }

    protected Cpp11LanguageVersion(String id, String presentableName) {
        super(id, presentableName);
        addFeature(CLanguageFeature.NULLPTR);
        addFeature(CLanguageFeature.CONSTEXPR);
        addFeature(CLanguageFeature.STATIC_ASSERT);
        addFeature(CLanguageFeature.ALIGNMENT);
        addFeature(CLanguageFeature.THREAD_LOCAL);
        addFeature(CLanguageFeature.DECLTYPE);
        addFeature(CLanguageFeature.NOEXCEPT);
        addFeature(CLanguageFeature.UNICODE_CHAR_TYPES);
        addFeature(CLanguageFeature.RAW_STRINGS);
    }

    @Override
    public int getWeight() {
        return 2011;
    }

    @Override
    public String getVersionMacroValue() {
        return "201103L";
    }
}
