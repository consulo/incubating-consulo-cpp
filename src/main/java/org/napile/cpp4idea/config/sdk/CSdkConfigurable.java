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

import org.jetbrains.annotations.Nls;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.MasterDetailsComponent;

/**
 * @author VISTALL
 * @date 16:56/11.12.2011
 */
public class CSdkConfigurable extends MasterDetailsComponent implements AdditionalDataConfigurable
{
	@Override
	protected void processRemovedItems()
	{

	}

	@Override
	protected boolean wasObjectStored(Object editableObject)
	{
		return false;
	}

	@Nls
	@Override
	public String getDisplayName()
	{
		return "C/C++";
	}

	@Override
	public Icon getIcon()
	{
		return CIcons.SOURCE_FILE;
	}

	@Override
	public void setSdk(Sdk sdk)
	{

	}
}
