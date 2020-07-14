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

package org.napile.cpp4idea.lang.psiInitial.visitors;

import org.napile.cpp4idea.lang.psiInitial.CPsiSharpDefineValue;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpElement;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;
import org.napile.cpp4idea.lang.psiInitial.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpDefine;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpIfBody;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpIfDef;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpInclude;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpIndepInclude;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 13:07/16.12.2011
 */
public class CSharpPsiElementVisitor extends PsiElementVisitor
{
	public void visitSFile(CPsiSharpFile file)
	{
		visitFile(file);
	}

	public void visitSElement(CPsiSharpElement element)
	{
		visitElement(element);
	}

	public void visitCompilerVariable(CPsiCompilerVariable element)
	{
		visitSElement(element);
	}

	public void visitSIfBody(CPsiSharpIfBody element)
	{
		visitSElement(element);
	}

	public void visitSDefine(CPsiSharpDefine element)
	{
		visitSElement(element);
	}

	public void visitSDefineValue(CPsiSharpDefineValue element)
	{
		visitSElement(element);
	}

	public void visitSIfDef(CPsiSharpIfDef element)
	{
		visitSElement(element);
	}

	public void visitSInclude(CPsiSharpInclude element)
	{
		visitSElement(element);
	}

	public void visitSIndependInclude(CPsiSharpIndepInclude element)
	{
		visitSElement(element);
	}
}
