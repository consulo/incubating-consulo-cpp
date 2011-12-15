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

package org.napile.cpp4idea.lang.psi.visitors;

import org.napile.cpp4idea.lang.psi.*;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 17:36/11.12.2011
 */
@Deprecated
public class CPsiVisitor
{
	public void visitElement(PsiElement element, CPsiCompilerVariableHolder variableHolder)
	{
	}

	public void visitFile(CPsiGenFile genFile, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(genFile, variableHolder);
	}

	public void visitFile(CPsiBinaryFile binaryFile, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(binaryFile, variableHolder);
	}

	public void visitDefine(CPsiDefine define, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(define, variableHolder);
	}

	public void visitInclude(CPsiInclude include, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(include, variableHolder);
	}

	public void visitIndependInclude(CPsiIndependInclude include, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(include, variableHolder);
	}

	public void visitIfDefHolder(CPsiIfDefHolder element, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(element, variableHolder);
	}

	public void visitIfNotDefHolder(CPsiIfNotDefHolder element, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(element, variableHolder);
	}

	public void visitCompilerVariable(CPsiCompilerVariable element, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(element, variableHolder);
	}

	public void visitEnum(CPsiEnum element, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(element, variableHolder);
	}

	public void visitEnumConstant(CPsiEnumConstant element, CPsiCompilerVariableHolder variableHolder)
	{
		visitElement(element, variableHolder);
	}
}
