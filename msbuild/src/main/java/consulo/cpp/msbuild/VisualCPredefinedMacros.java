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
package consulo.cpp.msbuild;

import consulo.cpp.lang.version.CLanguageVersion;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
public final class VisualCPredefinedMacros {
    private static final Pattern MSVC_TOOLSET = Pattern.compile("v\\d{3}(_xp)?", Pattern.CASE_INSENSITIVE);

    private static final Map<String, String> TOOLSET_MSC_VER = Map.of(
        "v100", "1600",
        "v110", "1700",
        "v120", "1800",
        "v140", "1900",
        "v141", "1916",
        "v142", "1929",
        "v143", "1944",
        "v145", "1950"
    );

    private static final Map<String, String> VISUAL_STUDIO_MSC_VER = Map.of(
        "10", "1600",
        "11", "1700",
        "12", "1800",
        "14", "1900",
        "15", "1916",
        "16", "1929",
        "17", "1944",
        "18", "1950"
    );

    private static final String LEGACY_CPLUSPLUS = "199711L";

    private VisualCPredefinedMacros() {
    }

    public static void fill(Map<String, String> defines, VisualCProjectModel model, VisualCCompileSettings settings) {
        String toolset = model.platformToolset().toLowerCase(Locale.ROOT);
        if (!MSVC_TOOLSET.matcher(toolset).matches() && !"clangcl".equals(toolset)) {
            return;
        }

        fillPlatform(defines, model.platform());

        String mscVer = mscVer(toolset, model.visualStudioVersion());
        if (mscVer != null) {
            defines.putIfAbsent("_MSC_VER", mscVer);
        }

        fillRuntimeLibrary(defines, settings.runtimeLibrary());

        CLanguageVersion cppStandard = settings.cppStandard();
        CLanguageVersion cStandard = settings.cStandard();
        if (settings.hasCpp() && cppStandard != null) {
            String standard = cppStandard.getVersionMacroValue();
            defines.putIfAbsent("_MSVC_LANG", standard);
            defines.putIfAbsent("__cplusplus", settings.conformingCplusplus() ? standard : LEGACY_CPLUSPLUS);
        }
        else if (settings.hasC() && cStandard != null) {
            String standard = cStandard.getVersionMacroValue();
            if (!standard.isEmpty()) {
                defines.putIfAbsent("__STDC_VERSION__", standard);
            }
        }
    }

    private static void fillPlatform(Map<String, String> defines, String platform) {
        switch (platform.toLowerCase(Locale.ROOT)) {
            case "win32", "x86" -> {
                defines.putIfAbsent("_WIN32", "1");
                defines.putIfAbsent("_M_IX86", "600");
            }
            case "x64" -> {
                defines.putIfAbsent("_WIN32", "1");
                defines.putIfAbsent("_WIN64", "1");
                defines.putIfAbsent("_M_X64", "100");
                defines.putIfAbsent("_M_AMD64", "100");
            }
            case "arm64" -> {
                defines.putIfAbsent("_WIN32", "1");
                defines.putIfAbsent("_WIN64", "1");
                defines.putIfAbsent("_M_ARM64", "1");
            }
            case "arm" -> {
                defines.putIfAbsent("_WIN32", "1");
                defines.putIfAbsent("_M_ARM", "7");
            }
            default -> {
            }
        }
    }

    private static void fillRuntimeLibrary(Map<String, String> defines, String runtimeLibrary) {
        if (runtimeLibrary.isEmpty()) {
            return;
        }
        String runtime = runtimeLibrary.toLowerCase(Locale.ROOT);
        defines.putIfAbsent("_MT", "1");
        if (runtime.contains("debug")) {
            defines.putIfAbsent("_DEBUG", "1");
        }
        if (runtime.endsWith("dll")) {
            defines.putIfAbsent("_DLL", "1");
        }
    }

    private static @Nullable String mscVer(String toolset, String visualStudioVersion) {
        String byToolset = TOOLSET_MSC_VER.get(toolset.endsWith("_xp") ? toolset.substring(0, toolset.length() - 3) : toolset);
        if (byToolset != null) {
            return byToolset;
        }
        int dot = visualStudioVersion.indexOf('.');
        return VISUAL_STUDIO_MSC_VER.get(dot > 0 ? visualStudioVersion.substring(0, dot) : visualStudioVersion);
    }
}
