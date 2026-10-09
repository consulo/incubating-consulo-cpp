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

import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.MutableModuleExtension;
import consulo.ui.Component;
import consulo.ui.Label;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CMutableModuleExtension extends CModuleExtension implements MutableModuleExtension<CModuleExtension> {
    public CMutableModuleExtension(String id, ModuleRootLayer moduleRootLayer) {
        super(id, moduleRootLayer);
    }

    public void setDefines(Map<String, String> defines) {
        myDefines = new TreeMap<>(defines);
    }

    public void setIncludePaths(List<String> includePaths) {
        myIncludePaths = new ArrayList<>(includePaths);
    }

    @Override
    public void setEnabled(boolean val) {
        myIsEnabled = val;
    }

    @Override
    public boolean isModified(CModuleExtension originalExtension) {
        return myIsEnabled != originalExtension.isEnabled()
            || !myDefines.equals(originalExtension.getDefines())
            || !myIncludePaths.equals(originalExtension.getIncludePaths());
    }

    @RequiredUIAccess
    @Nullable
    @Override
    public Component createConfigurationComponent(Disposable uiDisposable, Runnable updateOnCheck) {
        VerticalLayout layout = VerticalLayout.create();
        layout.add(Label.create(LocalizeValue.localizeTODO("Defines (NAME=VALUE; NAME2)")));

        TextBox definesBox = TextBox.create(formatDefines(myDefines));
        definesBox.addValueListener(event -> {
            setDefines(parseDefines(event.getValue()));
            updateOnCheck.run();
        });
        layout.add(definesBox);

        layout.add(Label.create(LocalizeValue.localizeTODO("Include directories (path; path)")));
        TextBox includeBox = TextBox.create(formatIncludePaths(myIncludePaths));
        includeBox.addValueListener(event -> {
            setIncludePaths(parseIncludePaths(event.getValue()));
            updateOnCheck.run();
        });
        layout.add(includeBox);
        return layout;
    }
}
