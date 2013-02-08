/*
 * Copyright 2010-2013 napile.org
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

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiSuperClass;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 12:50/07.01.13
 */
public class CPsiSuperClassImpl extends CPsiElementBaseImpl implements CPsiSuperClass
{
	public CPsiSuperClassImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public IElementType getAccessElementType()
	{
		PsiElement element = getAccessElement();
		return element == null ? null : element.getNode().getElementType();
	}

	@Override
	public PsiElement getAccessElement()
	{
		return findChildByType(CPsiTokens.ACCESS_MODIFIERS);
	}
}
