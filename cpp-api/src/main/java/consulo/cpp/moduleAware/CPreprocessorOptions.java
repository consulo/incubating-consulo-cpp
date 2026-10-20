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

import consulo.cpp.lang.version.CLanguageVersion;
import consulo.cpp.lang.version.CLanguageVersions;
import consulo.cpp.module.extension.CModuleExtension;
import consulo.virtualFileSystem.VirtualFile;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Everything that decides how a C/C++ file preprocesses and parses: the defines it is compiled with, the directories
 * its includes resolve against, a fingerprint of the files it pulls in, and the id of the language version it is
 * parsed with (empty when the module configures none). Two files sharing these share their indexed data, so this is
 * the option the module-aware index keys its variants on; the fingerprint is what makes an edit to an included
 * header re-index the files that include it.
 */
public record CPreprocessorOptions(Map<String, String> defines,
                                   List<String> includePaths,
                                   String includeFingerprint,
                                   String languageVersionId) {
    public static final CPreprocessorOptions EMPTY = new CPreprocessorOptions(Map.of(), List.of(), "", "");

    public CPreprocessorOptions {
        defines = Map.copyOf(new TreeMap<>(defines));
        includePaths = List.copyOf(includePaths);
        includeFingerprint = includeFingerprint == null ? "" : includeFingerprint;
        languageVersionId = languageVersionId == null ? "" : languageVersionId;
    }

    public CPreprocessorOptions(Map<String, String> defines, List<String> includePaths, String includeFingerprint) {
        this(defines, includePaths, includeFingerprint, "");
    }

    /**
     * The options {@code file} is preprocessed with in a module configured by {@code extension}.
     *
     * @param includeFingerprint the fingerprint of the files {@code file} includes - only computed when indexing
     */
    public static CPreprocessorOptions of(CModuleExtension<?> extension, VirtualFile file, String includeFingerprint) {
        return new CPreprocessorOptions(extension.getDefines(), extension.getIncludePaths(), includeFingerprint, languageVersionId(extension, file));
    }

    /**
     * The version the file is parsed with when its module configures a standard; empty otherwise, so modules which
     * configure nothing keep sharing their index.
     */
    private static String languageVersionId(CModuleExtension<?> extension, VirtualFile file) {
        CLanguageVersion cStandard = extension.getCStandard();
        CLanguageVersion cppStandard = extension.getCppStandard();
        if (cStandard == null && cppStandard == null) {
            return "";
        }
        return CLanguageVersions.select(file.getFileType(), cStandard, cppStandard).getId();
    }

    public boolean isEmpty() {
        return defines.isEmpty() && includePaths.isEmpty() && includeFingerprint.isEmpty() && languageVersionId.isEmpty();
    }
}
