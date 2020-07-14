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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiDeclaration;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

/**
 * @author VISTALL
 * @date 17:42/07.01.13
 */
public class CPsiDeclarationImpl extends CPsiElementBaseImpl implements CPsiDeclaration {
	public CPsiDeclarationImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		return findChildByType(CPsiTokens.IDENTIFIER);
	}

	@Override
	public String getName() {
		PsiElement element = getNameIdentifier();
		return element == null ? null : element.getText();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
		return null;
	}
}
