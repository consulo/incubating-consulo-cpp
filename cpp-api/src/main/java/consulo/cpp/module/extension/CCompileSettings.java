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
package consulo.cpp.module.extension;

import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
public final class CCompileSettings {
    private static final String ATTR_C_STANDARD = "c-standard";
    private static final String ATTR_CPP_STANDARD = "cpp-standard";

    private Map<String, String> myDefines = new TreeMap<>();
    private List<String> myIncludePaths = new ArrayList<>();
    private CLanguageStandard myCStandard = CLanguageStandard.DEFAULT;
    private CLanguageStandard myCppStandard = CLanguageStandard.DEFAULT;

    public Map<String, String> getDefines() {
        return Map.copyOf(myDefines);
    }

    public void setDefines(Map<String, String> defines) {
        myDefines = new TreeMap<>(defines);
    }

    public List<String> getIncludePaths() {
        return List.copyOf(myIncludePaths);
    }

    public void setIncludePaths(List<String> includePaths) {
        myIncludePaths = new ArrayList<>(includePaths);
    }

    public CLanguageStandard getCStandard() {
        return myCStandard;
    }

    public void setCStandard(CLanguageStandard standard) {
        myCStandard = standard;
    }

    public CLanguageStandard getCppStandard() {
        return myCppStandard;
    }

    public void setCppStandard(CLanguageStandard standard) {
        myCppStandard = standard;
    }

    public void copyFrom(CCompileSettings settings) {
        myDefines = new TreeMap<>(settings.myDefines);
        myIncludePaths = new ArrayList<>(settings.myIncludePaths);
        myCStandard = settings.myCStandard;
        myCppStandard = settings.myCppStandard;
    }

    public void write(Element element) {
        if (myCStandard != CLanguageStandard.DEFAULT) {
            element.setAttribute(ATTR_C_STANDARD, myCStandard.name());
        }
        if (myCppStandard != CLanguageStandard.DEFAULT) {
            element.setAttribute(ATTR_CPP_STANDARD, myCppStandard.name());
        }
        CModuleExtension.writeDefines(element, myDefines);
        CModuleExtension.writeIncludePaths(element, myIncludePaths);
    }

    public void read(Element element) {
        myCStandard = readStandard(element.getAttributeValue(ATTR_C_STANDARD));
        myCppStandard = readStandard(element.getAttributeValue(ATTR_CPP_STANDARD));
        myDefines = new TreeMap<>(CModuleExtension.readDefines(element));
        myIncludePaths = new ArrayList<>(CModuleExtension.readIncludePaths(element));
    }

    private static CLanguageStandard readStandard(@Nullable String value) {
        if (value != null) {
            for (CLanguageStandard standard : CLanguageStandard.values()) {
                if (standard.name().equals(value)) {
                    return standard;
                }
            }
        }
        return CLanguageStandard.DEFAULT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CCompileSettings that)) {
            return false;
        }
        return myDefines.equals(that.myDefines)
            && myIncludePaths.equals(that.myIncludePaths)
            && myCStandard == that.myCStandard
            && myCppStandard == that.myCppStandard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myDefines, myIncludePaths, myCStandard, myCppStandard);
    }
}
