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

import org.napile.cpp4idea.editor.CProcessAnnotator;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @date 15:53/29.12.12
 */
public class DebugProcessedPsiTreeToolWindow extends AbstractDebugPsiTreeToolWindow
{
	public DebugProcessedPsiTreeToolWindow(Project project)
	{
		super(project);
	}

	@Override
	protected String toText(PsiFile psiFile)
	{
		CPsiFile cPsiFile = psiFile.getUserData(CProcessAnnotator.C_PROCESSED_FILE);

		if(cPsiFile == null)
			return "null";

		return debugger.toText(cPsiFile);
	}
}
