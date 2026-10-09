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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Everything that decides how a C/C++ file preprocesses: the defines it is compiled with, the directories its
 * includes resolve against, and a fingerprint of the files it pulls in. Two files sharing these share their
 * indexed data, so this is the option the module-aware index keys its variants on; the fingerprint is what makes
 * an edit to an included header re-index the files that include it.
 */
public record CPreprocessorOptions(Map<String, String> defines, List<String> includePaths, String includeFingerprint) {
    public static final CPreprocessorOptions EMPTY = new CPreprocessorOptions(Map.of(), List.of(), "");

    public CPreprocessorOptions {
        defines = Map.copyOf(new TreeMap<>(defines));
        includePaths = List.copyOf(includePaths);
        includeFingerprint = includeFingerprint == null ? "" : includeFingerprint;
    }

    public boolean isEmpty() {
        return defines.isEmpty() && includePaths.isEmpty() && includeFingerprint.isEmpty();
    }
}
