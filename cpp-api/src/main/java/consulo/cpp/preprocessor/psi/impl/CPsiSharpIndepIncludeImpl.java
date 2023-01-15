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

package consulo.cpp.preprocessor.psi.impl;

import consulo.cpp.preprocessor.psi.CPsiSharpIndepInclude;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorElementVisitor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 1:53/11.12.2011
 */
public class CPsiSharpIndepIncludeImpl extends CPsiSharpIncludeImpl implements CPsiSharpIndepInclude
{
	public CPsiSharpIndepIncludeImpl(@org.jetbrains.annotations.NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CPreprocessorElementVisitor)
		{
			((CPreprocessorElementVisitor) visitor).visitSIndependInclude(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public PsiElement getIncludeElement()
	{
		return findChildByType(CPsiTokens.STRING_INCLUDE_LITERAL);
	}
}
