/*
 * Copyright 2010-2012 napile.org
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

package org.napile.cpp4idea.ide.debugToolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

/**
 * @author VISTALL
 * @date 14:19/29.12.12
 */
public class DebugToolWindowFactory implements ToolWindowFactory
{
	@Override
	public void createToolWindowContent(Project project, ToolWindow toolWindow)
	{
		ContentManager contentManager = toolWindow.getContentManager();
		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
		contentManager.addContent(contentFactory.createContent(new DebugPsiTreeToolWindow(project), "Debug Psi", true));
		contentManager.addContent(contentFactory.createContent(new DebugProcessedPsiTreeToolWindow(project), "Debug Processed Psi", true));
	}
}
