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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiCodeBlock;
import org.napile.cpp4idea.lang.psi.CPsiImplementingMethod;
import org.napile.cpp4idea.lang.psi.CPsiParameterList;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 22:57/10.12.2011
 */
public class CPsiImplementingMethodImpl extends CPsiDeclarationImpl implements CPsiImplementingMethod {
	public CPsiImplementingMethodImpl(@org.jetbrains.annotations.NotNull ASTNode node) {
		super(node);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof CPsiElementVisitor) {
			((CPsiElementVisitor) visitor).visitImplementingMethod(this);
		}
		else {
			super.accept(visitor);
		}
	}

	@Override
	public CPsiParameterList getParameterList() {
		return findChildByClass(CPsiParameterList.class);
	}

	@Override
	@NotNull
	public CPsiCodeBlock getCodeBlock() {
		return findNotNullChildByClass(CPsiCodeBlock.class);
	}
}
