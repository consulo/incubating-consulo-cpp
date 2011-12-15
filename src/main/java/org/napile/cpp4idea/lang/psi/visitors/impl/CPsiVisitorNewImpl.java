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

package org.napile.cpp4idea.lang.psi.visitors.impl;

import org.napile.cpp4idea.lang.psi.CPsiBinaryFile;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariableHolder;
import org.napile.cpp4idea.lang.psi.CPsiVisitingElement;
import org.napile.cpp4idea.lang.psi.visitors.CPsiVisitorNew;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 19:51/15.12.2011
 */
public class CPsiVisitorNewImpl implements CPsiVisitorNew
{
	@Override
	public void visitElement(PsiElement element, boolean defined)
	{
		visitChildren(element, defined);
	}

	@Override
	public void visitSharpIfElement(CPsiCompilerVariableHolder holder, boolean value, boolean defined)
	{
		visitElement(holder, defined);
	}

	@Override
	public void visitBinaryFile(CPsiBinaryFile element, boolean defined)
	{
		visitElement(element, defined);
	}

	protected void visitChildren(PsiElement element, boolean defined)
	{
		for(PsiElement child : element.getChildren())
		{
			if(!(child instanceof CPsiVisitingElement))
				visitElement(child, defined);
			else 
				((CPsiVisitingElement)child).accept(this, defined);
		}
	}
}
