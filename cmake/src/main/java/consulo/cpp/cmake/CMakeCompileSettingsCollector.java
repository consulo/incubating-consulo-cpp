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
package consulo.cpp.cmake;

import consulo.cmake.externalSystem.model.CMakeCompileData;
import consulo.cpp.module.extension.CCompileSettings;
import consulo.cpp.module.extension.CLanguageStandard;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
final class CMakeCompileSettingsCollector {
    private static final Map<String, CLanguageStandard> C_STANDARDS = Map.of(
        "90", CLanguageStandard.C89,
        "99", CLanguageStandard.C99,
        "11", CLanguageStandard.C11,
        "17", CLanguageStandard.C17,
        "23", CLanguageStandard.C23
    );

    private static final Map<String, CLanguageStandard> CPP_STANDARDS = Map.of(
        "98", CLanguageStandard.CPP98,
        "11", CLanguageStandard.CPP11,
        "14", CLanguageStandard.CPP14,
        "17", CLanguageStandard.CPP17,
        "20", CLanguageStandard.CPP20,
        "23", CLanguageStandard.CPP23,
        "26", CLanguageStandard.CPP23
    );

    private final Set<String> myIncludePaths = new LinkedHashSet<>();
    private final Set<String> mySystemIncludePaths = new LinkedHashSet<>();
    private final Map<String, String> myDefines = new LinkedHashMap<>();
    private CLanguageStandard myCStandard = CLanguageStandard.DEFAULT;
    private CLanguageStandard myCppStandard = CLanguageStandard.DEFAULT;

    void add(CMakeCompileData data) {
        myIncludePaths.addAll(data.getIncludePaths());
        mySystemIncludePaths.addAll(data.getSystemIncludePaths());
        myDefines.putAll(data.getDefines());

        String language = data.getLanguage();
        if (CMakeCompileData.LANGUAGE_C.equals(language)) {
            myCStandard = newer(myCStandard, find(C_STANDARDS, data.getStandard()));
        }
        else if (CMakeCompileData.LANGUAGE_CXX.equals(language)) {
            myCppStandard = newer(myCppStandard, find(CPP_STANDARDS, data.getStandard()));
        }
    }

    CCompileSettings build() {
        Set<String> includePaths = new LinkedHashSet<>(myIncludePaths);
        includePaths.addAll(mySystemIncludePaths);

        CCompileSettings settings = new CCompileSettings();
        settings.setIncludePaths(new ArrayList<>(includePaths));
        settings.setDefines(myDefines);
        settings.setCStandard(myCStandard);
        settings.setCppStandard(myCppStandard);
        return settings;
    }

    private static CLanguageStandard find(Map<String, CLanguageStandard> standards, @Nullable String standard) {
        return standard == null ? CLanguageStandard.DEFAULT : standards.getOrDefault(standard, CLanguageStandard.DEFAULT);
    }

    private static CLanguageStandard newer(CLanguageStandard current, CLanguageStandard candidate) {
        return candidate.ordinal() > current.ordinal() ? candidate : current;
    }
}
