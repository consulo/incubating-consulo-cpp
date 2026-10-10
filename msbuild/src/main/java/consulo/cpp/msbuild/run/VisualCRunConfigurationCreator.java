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

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.TopicImpl;
import consulo.application.ReadAction;
import consulo.execution.RunManager;
import consulo.execution.RunnerAndConfigurationSettings;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.msbuild.MSBuildProjectListener;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
@TopicImpl(ComponentScope.PROJECT)
public class VisualCRunConfigurationCreator implements MSBuildProjectListener {
    private final Project myProject;

    @Inject
    public VisualCRunConfigurationCreator(Project project) {
        myProject = project;
    }

    @Override
    public void projectsReloaded() {
        List<Module> modules = new ArrayList<>();
        for (Module module : ReadAction.compute(() -> ModuleManager.getInstance(myProject).getModules())) {
            if (VisualCRunConfiguration.findRunnableExtension(module) != null) {
                modules.add(module);
            }
        }
        if (modules.isEmpty()) {
            return;
        }
        myProject.getUIAccess().give(() -> createConfigurations(modules));
    }

    @RequiredUIAccess
    private void createConfigurations(List<Module> modules) {
        if (myProject.isDisposed()) {
            return;
        }

        VisualCRunConfigurationType type = VisualCRunConfigurationType.getInstance(myProject.getApplication());
        RunManager runManager = RunManager.getInstance(myProject);
        if (!runManager.getConfigurationsList(type).isEmpty()) {
            return;
        }

        ConfigurationFactory factory = type.getConfigurationFactories()[0];
        RunnerAndConfigurationSettings first = null;
        for (Module module : modules) {
            if (module.isDisposed()) {
                continue;
            }
            RunnerAndConfigurationSettings settings = runManager.createRunConfiguration(module.getName(), factory);
            ((VisualCRunConfiguration) settings.getConfiguration()).setModule(module);
            runManager.addConfiguration(settings, false);
            if (first == null) {
                first = settings;
            }
        }

        if (first != null && runManager.getSelectedConfiguration() == null) {
            runManager.setSelectedConfiguration(first);
        }
    }
}
