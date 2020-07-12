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

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.napile.cpp4idea.config.sdk.ui.GeneralTabSdkPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author VISTALL
 * @date 16:56/11.12.2011
 */
public class CSdkConfigurable implements AdditionalDataConfigurable
{
	private JBTabsImpl _tabs;

	public CSdkConfigurable()
	{
		_tabs = new JBEditorTabs(null, null, null, Disposer.newDisposable());
		//_tabs.setTabSidePaintBorder(5);
		//_tabs.setPaintBorder(1, 1, 1, 1);

		_tabs.addTab(new TabInfo(new GeneralTabSdkPanel()).setText("General"));
		_tabs.addTab(new TabInfo(new JPanel()).setText("Include"));
		_tabs.addTab(new TabInfo(new JPanel()).setText("Lib"));
	}

	@Override
	public void setSdk(Sdk sdk)
	{

	}

	@Override
	public JComponent createComponent()
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(_tabs.getComponent(), BorderLayout.NORTH);
		return panel;
	}

	@Override
	public boolean isModified()
	{
		return false;
	}

	@Override
	public void apply() throws ConfigurationException
	{

	}

	@Override
	public void reset()
	{

	}

	@Override
	public void disposeUIResources()
	{
		
	}
}
