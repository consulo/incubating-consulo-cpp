/*
 * Copyright 2011 napile
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

package org.napile.cpp4idea.config.sdk;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.content.bundle.SdkType;
import consulo.ui.image.Image;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.config.sdk.sdkdialect.SdkDialect;
import org.napile.cpp4idea.util.CIcons;

/**
 * @author VISTALL
 * @since 5:41/11.12.2011
 */
@ExtensionImpl
public class CSdkType extends SdkType {
    public static SdkType getInstance() {
        return Application.get().getExtensionPoint(SdkType.class).findExtensionOrFail(CSdkType.class);
    }

    public CSdkType() {
        super("C");
    }

    @Override
    public boolean isValidSdkHome(String path) {
        for (SdkDialect dialect : SdkDialect.DIALECTS) {
            if (dialect.isSupported(path)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public String getVersionString(String s) {
        return null;
    }

    @Override
    public String suggestSdkName(String currentSdkName, String sdkHome) {
        return null;
    }

    @Override
    public String getPresentableName() {
        return "C/C++ SDK";
    }

    @Override
    public Image getIcon() {
        return CIcons.SourceFile;
    }
}
