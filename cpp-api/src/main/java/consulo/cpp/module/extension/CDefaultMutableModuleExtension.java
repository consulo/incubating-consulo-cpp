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

import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.ui.Component;
import consulo.ui.Label;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CDefaultMutableModuleExtension extends CDefaultModuleExtension implements CMutableModuleExtension<CDefaultModuleExtension> {
    public CDefaultMutableModuleExtension(String id, ModuleRootLayer moduleRootLayer) {
        super(id, moduleRootLayer);
    }

    @Override
    public void setDefines(Map<String, String> defines) {
        myCompileSettings.setDefines(defines);
    }

    @Override
    public void setIncludePaths(List<String> includePaths) {
        myCompileSettings.setIncludePaths(includePaths);
    }

    @Override
    public void setTarget(@Nullable String target) {
        myCompileSettings.setTarget(target);
    }

    @Override
    public void setEnabled(boolean val) {
        myIsEnabled = val;
    }

    @Override
    public boolean isModified(CDefaultModuleExtension originalExtension) {
        return myIsEnabled != originalExtension.isEnabled()
            || !myCompileSettings.equals(originalExtension.myCompileSettings);
    }

    @RequiredUIAccess
    @Nullable
    @Override
    public Component createConfigurationComponent(Disposable uiDisposable, Runnable updateOnCheck) {
        VerticalLayout layout = VerticalLayout.create();
        layout.add(Label.create(LocalizeValue.localizeTODO("Defines (NAME=VALUE; NAME2)")));

        TextBox definesBox = TextBox.create(CModuleExtension.formatDefines(getDefines()));
        definesBox.addValueListener(event -> {
            setDefines(CModuleExtension.parseDefines(event.getValue()));
            updateOnCheck.run();
        });
        layout.add(definesBox);

        layout.add(Label.create(LocalizeValue.localizeTODO("Include directories (path; path)")));
        TextBox includeBox = TextBox.create(CModuleExtension.formatIncludePaths(getIncludePaths()));
        includeBox.addValueListener(event -> {
            setIncludePaths(CModuleExtension.parseIncludePaths(event.getValue()));
            updateOnCheck.run();
        });
        layout.add(includeBox);

        layout.add(Label.create(LocalizeValue.localizeTODO("Target (x86_64-pc-linux-gnu, arm-none-eabi; empty - the IDE's machine)")));
        String target = getTarget();
        TextBox targetBox = TextBox.create(target == null ? "" : target);
        targetBox.addValueListener(event -> {
            setTarget(event.getValue());
            updateOnCheck.run();
        });
        layout.add(targetBox);
        return layout;
    }
}
