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

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.api.icon.CppApiIconGroup;
import consulo.cpp.module.extension.CDefaultModuleExtension;
import consulo.cpp.module.extension.CDefaultMutableModuleExtension;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.MutableModuleExtension;
import consulo.ui.image.Image;

@ExtensionImpl
public class CModuleExtensionProvider implements ModuleExtensionProvider<CDefaultModuleExtension> {
    @Override
    public String getId() {
        return CDefaultModuleExtension.ID;
    }

    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("C/C++");
    }

    @Override
    public Image getIcon() {
        return CppApiIconGroup.filetypeC();
    }

    @Override
    public ModuleExtension<CDefaultModuleExtension> createImmutableExtension(ModuleRootLayer layer) {
        return new CDefaultModuleExtension(getId(), layer);
    }

    @Override
    public MutableModuleExtension<CDefaultModuleExtension> createMutableExtension(ModuleRootLayer layer) {
        return new CDefaultMutableModuleExtension(getId(), layer);
    }
}
