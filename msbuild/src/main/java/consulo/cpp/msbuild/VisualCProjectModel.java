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

import consulo.msbuild.MSBuildImportUtil;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
public record VisualCProjectModel(String configuration,
                                  String platform,
                                  String configurationType,
                                  String platformToolset,
                                  String visualStudioVersion,
                                  Path projectDir,
                                  @Nullable Path solutionDir,
                                  @Nullable Path outDir,
                                  @Nullable Path intDir,
                                  @Nullable Path targetPath,
                                  @Nullable Path debuggerCommand,
                                  String debuggerArguments,
                                  Path debuggerWorkingDirectory,
                                  Map<String, String> debuggerEnvironment) {
    public static final String APPLICATION = "Application";

    public static VisualCProjectModel of(Path projectDir, Map<String, String> properties) {
        Path targetPath = path(projectDir, properties, "TargetPath");
        Path debuggerCommand = path(projectDir, properties, "LocalDebuggerCommand");
        Path debuggerWorkingDirectory = path(projectDir, properties, "LocalDebuggerWorkingDirectory");
        return new VisualCProjectModel(
            valueOrDefault(value(properties, "Configuration"), VisualCProjectFile.DEFAULT_CONFIGURATION),
            valueOrDefault(value(properties, "Platform"), VisualCProjectFile.DEFAULT_PLATFORM),
            value(properties, "ConfigurationType"),
            value(properties, "PlatformToolset"),
            value(properties, "VisualStudioVersion"),
            projectDir,
            path(projectDir, properties, "SolutionDir"),
            path(projectDir, properties, "OutDir"),
            path(projectDir, properties, "IntDir"),
            targetPath,
            debuggerCommand != null ? debuggerCommand : targetPath,
            value(properties, "LocalDebuggerCommandArguments"),
            debuggerWorkingDirectory != null ? debuggerWorkingDirectory : projectDir,
            parseEnvironment(value(properties, "LocalDebuggerEnvironment"))
        );
    }

    public List<Path> baseDirs() {
        Path solution = solutionDir;
        return solution == null || solution.equals(projectDir) ? List.of(projectDir) : List.of(projectDir, solution);
    }

    private static String value(Map<String, String> properties, String name) {
        String value = properties.get(name);
        return value == null ? "" : MSBuildImportUtil.expandProperties(value, properties).trim();
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value.isEmpty() ? defaultValue : value;
    }

    private static @Nullable Path path(Path projectDir, Map<String, String> properties, String name) {
        String value = value(properties, name);
        return value.isEmpty() ? null : MSBuildImportUtil.resolvePath(projectDir, value);
    }

    private static Map<String, String> parseEnvironment(String text) {
        Map<String, String> environment = new LinkedHashMap<>();
        for (String line : text.split("\\r?\\n")) {
            String trimmed = line.trim();
            int separator = trimmed.indexOf('=');
            if (separator > 0) {
                environment.put(trimmed.substring(0, separator).trim(), trimmed.substring(separator + 1).trim());
            }
        }
        return environment;
    }
}
