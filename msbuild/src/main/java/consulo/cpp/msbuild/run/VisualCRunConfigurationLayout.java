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
package consulo.cpp.msbuild.run;

import consulo.cpp.msbuild.localize.VisualCLocalize;
import consulo.execution.ui.CommonProgramParametersLayout;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;
import consulo.ui.ComboBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.dialog.DialogService;
import consulo.ui.util.FormBuilder;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
public class VisualCRunConfigurationLayout extends CommonProgramParametersLayout<VisualCRunConfiguration> {
    private final Project myProject;
    private @Nullable ComboBox<Module> myModuleBox;

    public VisualCRunConfigurationLayout(Project project) {
        super(project.getApplication().getInstance(DialogService.class));
        myProject = project;
    }

    @RequiredUIAccess
    @Override
    protected void addBefore(FormBuilder builder) {
        List<Module> modules = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(myProject).getModules()) {
            if (VisualCRunConfiguration.findRunnableExtension(module) != null) {
                modules.add(module);
            }
        }

        ComboBox<Module> moduleBox = ComboBox.create(modules);
        moduleBox.setRender((presentation, item) -> {
            Module module = item.getValue();
            if (module != null) {
                presentation.withIcon(PlatformIconGroup.nodesModule());
                presentation.append(module.getName());
            }
        });
        myModuleBox = moduleBox;

        builder.addLabeled(VisualCLocalize.runConfigurationModuleLabel(), moduleBox);
    }

    @Override
    protected Project getProject() {
        return myProject;
    }

    @RequiredUIAccess
    @Override
    public void apply(VisualCRunConfiguration configuration) {
        super.apply(configuration);

        ComboBox<Module> moduleBox = myModuleBox;
        Module module = moduleBox == null ? null : moduleBox.getValue();
        configuration.setModuleName(module == null ? null : module.getName());
    }

    @RequiredUIAccess
    @Override
    public void reset(VisualCRunConfiguration configuration) {
        super.reset(configuration);

        ComboBox<Module> moduleBox = myModuleBox;
        if (moduleBox != null) {
            moduleBox.setValue(configuration.getConfigurationModule().getModule());
        }
    }
}
