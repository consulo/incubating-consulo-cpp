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
package consulo.cpp.impl.module.extension;

import consulo.annotation.access.RequiredReadAction;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.content.layer.extension.ModuleExtensionBase;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Per-module preprocessor defines. These are the defines every C/C++ file of the module is indexed and parsed
 * under, and they are the value the module-aware index keys its variants on.
 */
public class CModuleExtension extends ModuleExtensionBase<CModuleExtension> {
    protected Map<String, String> myDefines = new TreeMap<>();
    protected List<String> myIncludePaths = new ArrayList<>();

    public CModuleExtension(String id, ModuleRootLayer moduleRootLayer) {
        super(id, moduleRootLayer);
    }

    public Map<String, String> getDefines() {
        return Map.copyOf(myDefines);
    }

    public List<String> getIncludePaths() {
        return List.copyOf(myIncludePaths);
    }

    public static String formatIncludePaths(List<String> includePaths) {
        return String.join("; ", includePaths);
    }

    public static List<String> parseIncludePaths(String text) {
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

    public static String formatDefines(Map<String, String> defines) {
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

    public static Map<String, String> parseDefines(String text) {
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

    @RequiredReadAction
    @Override
    public void commit(CModuleExtension mutableModuleExtension) {
        super.commit(mutableModuleExtension);
        myDefines = new TreeMap<>(mutableModuleExtension.myDefines);
        myIncludePaths = new ArrayList<>(mutableModuleExtension.myIncludePaths);
    }

    @Override
    protected void getStateImpl(Element element) {
        super.getStateImpl(element);
        for (Map.Entry<String, String> entry : myDefines.entrySet()) {
            element.addContent(new Element("define").setAttribute("name", entry.getKey()).setAttribute("value", entry.getValue()));
        }
        for (String path : myIncludePaths) {
            element.addContent(new Element("includePath").setAttribute("path", path));
        }
    }

    @RequiredReadAction
    @Override
    protected void loadStateImpl(Element element) {
        super.loadStateImpl(element);
        myDefines.clear();
        myIncludePaths.clear();
        for (Element child : element.getChildren("includePath")) {
            String path = child.getAttributeValue("path");
            if (path != null) {
                myIncludePaths.add(path);
            }
        }
        for (Element child : element.getChildren("define")) {
            String name = child.getAttributeValue("name");
            if (name != null) {
                String value = child.getAttributeValue("value");
                myDefines.put(name, value == null ? "" : value);
            }
        }
    }
}
