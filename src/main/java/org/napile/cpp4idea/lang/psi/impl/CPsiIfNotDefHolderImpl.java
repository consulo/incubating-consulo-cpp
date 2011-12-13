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
 *    limitations under the License.
 */

package org.napile.cpp4idea.lang.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import org.napile.cpp4idea.lang.psi.CPsiIfNotDefHolder;
import org.napile.cpp4idea.lang.psi.visitors.CPsiVisitor;
import com.intellij.lang.ASTNode;

/**
 * @author VISTALL
 * @date 14:07/11.12.2011
 */
public class CPsiIfNotDefHolderImpl extends CPsiElementBaseImpl implements CPsiIfNotDefHolder
{
	public CPsiIfNotDefHolderImpl(@org.jetbrains.annotations.NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(CPsiVisitor visitor)
	{
		visitor.visitIfNotDefHolder(this);
	}

	@Override
	public CPsiElement[] getElements()
	{
		return findChildrenByClass(CPsiElement.class);
	}

	@Override
	@NotNull
	public CPsiCompilerVariable getVariable()
	{
		return findNotNullChildByClass(CPsiCompilerVariable.class);
	}
}
