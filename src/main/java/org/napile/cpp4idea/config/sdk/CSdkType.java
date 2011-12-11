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
 *    limitations under the License.
 */

package org.napile.cpp4idea.config.sdk;

import javax.swing.Icon;

import org.jdom.Element;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;

/**
 * @author VISTALL
 * @date 5:41/11.12.2011
 */
public class CSdkType extends SdkType
{
	public static SdkType getInstance()
	{
		return SdkType.findInstance(CSdkType.class);
	}

	public CSdkType()
	{
		super("C");
	}

	@Override
	public String suggestHomePath()
	{
		return null;
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		return false;
	}

	@Override
	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		return null;
	}

	@Override
	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator)
	{
		return null;
	}

	@Override
	public void saveAdditionalData(SdkAdditionalData additionalData, Element additional)
	{

	}

	@Override
	public String getPresentableName()
	{
		return "C/C++ SDK";
	}

	@Override
	public Icon getIconForAddAction()
	{
		return CIcons.SOURCE_FILE;
	}
}
