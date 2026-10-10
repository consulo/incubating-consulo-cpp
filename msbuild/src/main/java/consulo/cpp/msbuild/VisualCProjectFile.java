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

import consulo.annotation.component.ExtensionImpl;
import consulo.logging.Logger;
import consulo.msbuild.MSBuildProjectConfiguration;
import consulo.msbuild.MSBuildProjectFile;
import consulo.util.jdom.JDOMUtil;
import consulo.virtualFileSystem.VirtualFile;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
@ExtensionImpl
public class VisualCProjectFile implements MSBuildProjectFile {
    public static final String EXTENSION = "vcxproj";
    public static final String CL_COMPILE_ITEM = "ClCompile";
    public static final String CL_INCLUDE_ITEM = "ClInclude";

    public static final String DEFAULT_CONFIGURATION = "Debug";
    public static final String DEFAULT_PLATFORM = "x64";
    private static final List<String> PREFERRED_PLATFORMS = List.of("x64", "Win32", "ARM64");

    private static final Logger LOG = Logger.getInstance(VisualCProjectFile.class);

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public Set<String> getCapabilities() {
        return Set.of(VisualCProjectCapability.ID);
    }

    @Override
    public Set<String> getItemTypes() {
        return Set.of(CL_COMPILE_ITEM, CL_INCLUDE_ITEM);
    }

    @Override
    public String getConfiguration() {
        return DEFAULT_CONFIGURATION;
    }

    @Override
    public String getPlatform() {
        return DEFAULT_PLATFORM;
    }

    @Override
    public MSBuildProjectConfiguration getProjectConfiguration(VirtualFile projectFile) {
        List<MSBuildProjectConfiguration> configurations = readProjectConfigurations(projectFile);
        MSBuildProjectConfiguration debug = selectByPlatform(configurations, DEFAULT_CONFIGURATION);
        if (debug != null) {
            return debug;
        }
        MSBuildProjectConfiguration any = selectByPlatform(configurations, null);
        return any != null ? any : new MSBuildProjectConfiguration(DEFAULT_CONFIGURATION, DEFAULT_PLATFORM);
    }

    private static @Nullable MSBuildProjectConfiguration selectByPlatform(List<MSBuildProjectConfiguration> configurations,
                                                                          @Nullable String configurationName) {
        List<MSBuildProjectConfiguration> candidates = new ArrayList<>();
        for (MSBuildProjectConfiguration configuration : configurations) {
            if (configurationName == null || configurationName.equalsIgnoreCase(configuration.configuration())) {
                candidates.add(configuration);
            }
        }
        for (String platform : PREFERRED_PLATFORMS) {
            for (MSBuildProjectConfiguration candidate : candidates) {
                if (platform.equalsIgnoreCase(candidate.platform())) {
                    return candidate;
                }
            }
        }
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    private static List<MSBuildProjectConfiguration> readProjectConfigurations(VirtualFile projectFile) {
        Element root;
        try (InputStream stream = projectFile.getInputStream()) {
            root = JDOMUtil.load(stream);
        }
        catch (IOException | JDOMException e) {
            LOG.warn("Cannot read project configurations of " + projectFile.getPath(), e);
            return List.of();
        }
        if (root == null) {
            return List.of();
        }

        List<MSBuildProjectConfiguration> configurations = new ArrayList<>();
        for (Element itemGroup : root.getChildren()) {
            if (!"ItemGroup".equals(itemGroup.getName())) {
                continue;
            }
            for (Element item : itemGroup.getChildren()) {
                if (!"ProjectConfiguration".equals(item.getName())) {
                    continue;
                }
                String include = item.getAttributeValue("Include");
                if (include == null) {
                    continue;
                }
                int separator = include.indexOf('|');
                if (separator > 0 && separator < include.length() - 1) {
                    configurations.add(new MSBuildProjectConfiguration(include.substring(0, separator), include.substring(separator + 1)));
                }
            }
        }
        return configurations;
    }
}
