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

import consulo.application.ReadAction;
import consulo.cpp.msbuild.localize.VisualCLocalize;
import consulo.cpp.msbuild.module.VisualCModuleExtension;
import consulo.execution.CommonProgramRunConfigurationParameters;
import consulo.execution.configuration.CommandLineState;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.EnvironmentVariablesData;
import consulo.execution.configuration.ModuleBasedConfiguration;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunConfigurationModule;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.module.Module;
import consulo.nativeDev.debugger.NativeDebuggableRunProfile;
import consulo.nativeDev.debugger.NativeDebuggerProvider;
import consulo.nativeDev.debugger.dap.CppdbgNativeDebuggerProvider;
import consulo.nativeDev.debugger.driver.NativeDebugTarget;
import consulo.platform.Platform;
import consulo.platform.PlatformOperatingSystem;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilderFactory;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.cmd.ParametersListUtil;
import consulo.util.lang.StringUtil;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.WriteExternalException;
import org.jdom.Element;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
public class VisualCRunConfiguration extends ModuleBasedConfiguration<RunConfigurationModule>
    implements CommonProgramRunConfigurationParameters, NativeDebuggableRunProfile {
    private static final String ATTR_PROGRAM_PARAMETERS = "program-parameters";
    private static final String ATTR_WORKING_DIRECTORY = "working-directory";
    private static final Pattern ENVIRONMENT_REFERENCE = Pattern.compile("%([^%]+)%");

    private @Nullable String myProgramParameters;
    private @Nullable String myWorkingDirectory;
    private EnvironmentVariablesData myEnvironmentVariables = EnvironmentVariablesData.DEFAULT;

    public VisualCRunConfiguration(String name, RunConfigurationModule configurationModule, ConfigurationFactory factory) {
        super(name, configurationModule, factory);
    }

    public static @Nullable VisualCModuleExtension findRunnableExtension(Module module) {
        VisualCModuleExtension extension = ReadAction.compute(() -> module.getExtension(VisualCModuleExtension.class));
        return extension != null && extension.isRunnable() ? extension : null;
    }

    @Override
    public Collection<Module> getValidModules() {
        List<Module> modules = new ArrayList<>();
        for (Module module : getAllModules()) {
            if (findRunnableExtension(module) != null) {
                modules.add(module);
            }
        }
        return modules;
    }

    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new VisualCRunConfigurationEditor(getProject());
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationError {
        Module module = getConfigurationModule().getModule();
        if (module == null) {
            throw new RuntimeConfigurationError(VisualCLocalize.runConfigurationModuleNotSpecified());
        }
        if (findRunnableExtension(module) == null) {
            throw new RuntimeConfigurationError(VisualCLocalize.runConfigurationNotRunnable(module.getName()));
        }
    }

    @Override
    public @Nullable RunProfileState getState(Executor executor, ExecutionEnvironment environment) throws ExecutionException {
        VisualCLaunchTarget target = createLaunchTarget();
        return new CommandLineState(environment) {
            @Override
            protected ProcessHandler startProcess() throws ExecutionException {
                return getProject().getApplication().getInstance(ProcessHandlerBuilderFactory.class)
                    .newBuilder(createCommandLine(target))
                    .killable()
                    .colored()
                    .build();
            }
        };
    }

    @Override
    public NativeDebugTarget createDebugTarget(ExecutionEnvironment environment) throws ExecutionException {
        VisualCLaunchTarget target = createLaunchTarget();
        return NativeDebugTarget.launch(target.executable(), target.arguments(), target.workingDirectory(), target.environment(), null);
    }

    @Override
    public @Nullable String getDebuggerProviderId() {
        if (!Platform.current().os().isWindows()) {
            return null;
        }
        Module module = getConfigurationModule().getModule();
        VisualCModuleExtension extension = module == null ? null : findRunnableExtension(module);
        String debuggerCommand = extension == null ? null : extension.getDebuggerCommand();
        if (debuggerCommand == null || !debuggerCommand.toLowerCase(Locale.ROOT).endsWith(".exe")) {
            return null;
        }
        NativeDebuggerProvider provider = getProject().getApplication()
            .getExtensionPoint(NativeDebuggerProvider.class)
            .findExtension(CppdbgNativeDebuggerProvider.class);
        return provider != null && !provider.findInstallations().isEmpty() ? provider.getId() : null;
    }

    public GeneralCommandLine createCommandLine(VisualCLaunchTarget target) {
        GeneralCommandLine commandLine = new GeneralCommandLine()
            .withExecutablePath(target.executable())
            .withWorkingDirectory(target.workingDirectory())
            .withEnvironment(target.environment());
        commandLine.addParameters(target.arguments());
        commandLine.withParentEnvironmentType(myEnvironmentVariables.isPassParentEnvs()
            ? GeneralCommandLine.ParentEnvironmentType.CONSOLE
            : GeneralCommandLine.ParentEnvironmentType.NONE);
        return commandLine;
    }

    public VisualCLaunchTarget createLaunchTarget() throws ExecutionException {
        Module module = getConfigurationModule().getModule();
        VisualCModuleExtension extension = module == null ? null : findRunnableExtension(module);
        String debuggerCommand = extension == null ? null : extension.getDebuggerCommand();
        if (extension == null || debuggerCommand == null) {
            throw new ExecutionException(module == null
                ? VisualCLocalize.runConfigurationModuleNotSpecified().get()
                : VisualCLocalize.runConfigurationNotRunnable(module.getName()).get());
        }

        Path executable = Path.of(debuggerCommand);

        String programParameters = StringUtil.isEmptyOrSpaces(myProgramParameters) ? extension.getDebuggerArguments() : myProgramParameters;
        List<String> arguments = ParametersListUtil.parse(StringUtil.notNullize(programParameters));

        String workingDirectory = StringUtil.isEmptyOrSpaces(myWorkingDirectory) ? extension.getDebuggerWorkingDirectory() : myWorkingDirectory;
        Path executableDirectory = executable.getParent();
        Path workingDirectoryPath = !StringUtil.isEmptyOrSpaces(workingDirectory)
            ? Path.of(workingDirectory)
            : executableDirectory != null ? executableDirectory : executable;

        Map<String, String> environment = new LinkedHashMap<>();
        PlatformOperatingSystem os = Platform.current().os();
        for (Map.Entry<String, String> entry : extension.getDebuggerEnvironment().entrySet()) {
            environment.put(entry.getKey(), expandEnvironmentReferences(entry.getValue(), os));
        }
        environment.putAll(myEnvironmentVariables.getEnvs());

        return new VisualCLaunchTarget(executable, arguments, workingDirectoryPath, environment);
    }

    private static String expandEnvironmentReferences(String value, PlatformOperatingSystem os) {
        Matcher matcher = ENVIRONMENT_REFERENCE.matcher(value);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String variable = os.getEnvironmentVariable(matcher.group(1));
            matcher.appendReplacement(builder, Matcher.quoteReplacement(variable != null ? variable : matcher.group()));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    @Override
    public void setProgramParameters(@Nullable String value) {
        myProgramParameters = value;
    }

    @Override
    public @Nullable String getProgramParameters() {
        return myProgramParameters;
    }

    @Override
    public void setWorkingDirectory(@Nullable String value) {
        myWorkingDirectory = value;
    }

    @Override
    public @Nullable String getWorkingDirectory() {
        return myWorkingDirectory;
    }

    @Override
    public void setEnvs(Map<String, String> envs) {
        myEnvironmentVariables = myEnvironmentVariables.with(envs);
    }

    @Override
    public Map<String, String> getEnvs() {
        return myEnvironmentVariables.getEnvs();
    }

    @Override
    public void setPassParentEnvs(boolean passParentEnvs) {
        myEnvironmentVariables = myEnvironmentVariables.with(passParentEnvs);
    }

    @Override
    public boolean isPassParentEnvs() {
        return myEnvironmentVariables.isPassParentEnvs();
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        myProgramParameters = element.getAttributeValue(ATTR_PROGRAM_PARAMETERS);
        myWorkingDirectory = element.getAttributeValue(ATTR_WORKING_DIRECTORY);
        myEnvironmentVariables = EnvironmentVariablesData.readExternal(element);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        if (myProgramParameters != null) {
            element.setAttribute(ATTR_PROGRAM_PARAMETERS, myProgramParameters);
        }
        if (myWorkingDirectory != null) {
            element.setAttribute(ATTR_WORKING_DIRECTORY, myWorkingDirectory);
        }
        myEnvironmentVariables.writeExternal(element);
    }
}
