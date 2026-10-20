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
package consulo.cpp.cmake.module;

import consulo.annotation.access.RequiredReadAction;
import consulo.cpp.module.extension.CCompileSettings;
import consulo.cpp.lang.version.CLanguageVersion;
import consulo.cpp.module.extension.CModuleExtension;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.content.layer.extension.ModuleExtensionBase;
import org.jdom.Element;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
public class CMakeCModuleExtension extends ModuleExtensionBase<CMakeCModuleExtension> implements CModuleExtension<CMakeCModuleExtension> {
    public static final String ID = "cmake-cpp";

    protected final CCompileSettings myCompileSettings = new CCompileSettings();

    public CMakeCModuleExtension(String id, ModuleRootLayer rootLayer) {
        super(id, rootLayer);
    }

    @Override
    public Map<String, String> getDefines() {
        return myCompileSettings.getDefines();
    }

    @Override
    public List<String> getIncludePaths() {
        return myCompileSettings.getIncludePaths();
    }

    @Override
    public @Nullable CLanguageVersion getCStandard() {
        return myCompileSettings.getCStandard();
    }

    @Override
    public @Nullable CLanguageVersion getCppStandard() {
        return myCompileSettings.getCppStandard();
    }

    @RequiredReadAction
    @Override
    public void commit(CMakeCModuleExtension mutableModuleExtension) {
        super.commit(mutableModuleExtension);
        myCompileSettings.copyFrom(mutableModuleExtension.myCompileSettings);
    }

    @Override
    protected void getStateImpl(Element element) {
        super.getStateImpl(element);
        myCompileSettings.write(element);
    }

    @RequiredReadAction
    @Override
    protected void loadStateImpl(Element element) {
        super.loadStateImpl(element);
        myCompileSettings.read(element);
    }
}
