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

import consulo.module.extension.ModuleExtension;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
public interface CModuleExtension<T extends CModuleExtension<T>> extends ModuleExtension<T> {
    String ELEMENT_DEFINE = "define";
    String ELEMENT_INCLUDE_PATH = "includePath";

    Map<String, String> getDefines();

    List<String> getIncludePaths();

    CLanguageStandard getCStandard();

    CLanguageStandard getCppStandard();

    static String formatIncludePaths(List<String> includePaths) {
        return String.join("; ", includePaths);
    }

    static List<String> parseIncludePaths(String text) {
        List<String> paths = new ArrayList<>();
        if (text == null) {
            return paths;
        }
        for (String part : text.split(";")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                paths.add(trimmed);
            }
        }
        return paths;
    }

    static String formatDefines(Map<String, String> defines) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : defines.entrySet()) {
            if (builder.length() != 0) {
                builder.append("; ");
            }
            builder.append(entry.getKey());
            if (!entry.getValue().isEmpty()) {
                builder.append('=').append(entry.getValue());
            }
        }
        return builder.toString();
    }

    static Map<String, String> parseDefines(String text) {
        Map<String, String> defines = new LinkedHashMap<>();
        if (text == null) {
            return defines;
        }
        for (String part : text.split(";")) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            int eq = trimmed.indexOf('=');
            if (eq < 0) {
                defines.put(trimmed, "");
            }
            else {
                defines.put(trimmed.substring(0, eq).trim(), trimmed.substring(eq + 1).trim());
            }
        }
        return defines;
    }

    static void writeDefines(Element element, Map<String, String> defines) {
        for (Map.Entry<String, String> entry : defines.entrySet()) {
            element.addContent(new Element(ELEMENT_DEFINE).setAttribute("name", entry.getKey()).setAttribute("value", entry.getValue()));
        }
    }

    static Map<String, String> readDefines(Element element) {
        Map<String, String> defines = new LinkedHashMap<>();
        for (Element child : element.getChildren(ELEMENT_DEFINE)) {
            String name = child.getAttributeValue("name");
            if (name != null) {
                String value = child.getAttributeValue("value");
                defines.put(name, value == null ? "" : value);
            }
        }
        return defines;
    }

    static void writeIncludePaths(Element element, List<String> includePaths) {
        for (String path : includePaths) {
            element.addContent(new Element(ELEMENT_INCLUDE_PATH).setAttribute("path", path));
        }
    }

    static List<String> readIncludePaths(Element element) {
        List<String> includePaths = new ArrayList<>();
        for (Element child : element.getChildren(ELEMENT_INCLUDE_PATH)) {
            String path = child.getAttributeValue("path");
            if (path != null) {
                includePaths.add(path);
            }
        }
        return includePaths;
    }
}
