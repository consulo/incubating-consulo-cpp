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

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiModifierList;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;
import consulo.language.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 12:31/02.01.13
 */
public class CPsiModifierListImpl extends CPsiElementBaseImpl implements CPsiModifierList {
	public CPsiModifierListImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public boolean hasModifier(@NotNull IElementType e) {
		return findChildByType(e) != null;
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof CPsiElementVisitor) {
			((CPsiElementVisitor) visitor).visitModifierList(this);
		} else {
			super.accept(visitor);
		}
	}
}
