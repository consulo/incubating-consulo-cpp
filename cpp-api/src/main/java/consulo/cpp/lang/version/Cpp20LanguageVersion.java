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
 * ISO C++20.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
@ExtensionImpl
public class Cpp20LanguageVersion extends Cpp17LanguageVersion {
    public static final String ID = "CPP20";

    @Inject
    public Cpp20LanguageVersion() {
        this(ID, "C++20");
    }

    protected Cpp20LanguageVersion(String id, String presentableName) {
        super(id, presentableName);
    }

    @Override
    public int getWeight() {
        return 2020;
    }

    @Override
    public String getVersionMacroValue() {
        return "202002L";
    }
}
