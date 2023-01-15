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

package org.napile.cpp4idea.lang.psi.impl;

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiEnumConstant;
import org.napile.cpp4idea.lang.psi.CPsiExpression;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;

/**
 * @author VISTALL
 * @date 17:51/14.12.2011
 */
public class CPsiEnumConstantImpl extends CPsiElementBaseImpl implements CPsiEnumConstant
{
	public CPsiEnumConstantImpl(@org.jetbrains.annotations.NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CPsiElementVisitor)
		{
			((CPsiElementVisitor) visitor).visitEnumConstant(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public String getName()
	{
		PsiElement element = getNameIdentifier();
		return element == null ? null : element.getText();
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(CPsiTokens.IDENTIFIER);
	}

	@Override
	public CPsiExpression getExpression()
	{
		return findChildByClass(CPsiExpression.class);
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		return null;
	}
}
