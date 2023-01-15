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

package consulo.cpp.preprocessor.psi.impl.visitor;

import consulo.cpp.preprocessor.psi.*;
import consulo.language.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @since 13:07/16.12.2011
 */
public class CPreprocessorElementVisitor extends PsiElementVisitor
{
	public void visitPreprocessorFile(CPreprocessorFile file)
	{
		visitFile(file);
	}

	public void visitSElement(CPreprocessorElement element)
	{
		visitElement(element);
	}

	public void visitSIfBody(CPsiSharpIfBody element)
	{
		visitSElement(element);
	}

	public void visitSDefine(CPreprocessorDefineDirective element)
	{
		visitSElement(element);
	}

	public void visitSDefineValue(CPsiSharpDefineValue element)
	{
		visitSElement(element);
	}

	public void visitPreprocessorIfBlock(CPreprocessorIfBlock element)
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
