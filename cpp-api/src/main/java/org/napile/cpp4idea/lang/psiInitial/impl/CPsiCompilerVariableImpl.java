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

package org.napile.cpp4idea.lang.psiInitial.impl;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import org.napile.cpp4idea.lang.psi.impl.CPsiElementBaseImpl;
import org.napile.cpp4idea.lang.psiInitial.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psiInitial.visitors.CSharpPsiElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 17:30/11.12.2011
 */
public class CPsiCompilerVariableImpl extends CPsiElementBaseImpl implements CPsiCompilerVariable {
	public CPsiCompilerVariableImpl(@org.jetbrains.annotations.NotNull ASTNode node) {
		super(node);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof CSharpPsiElementVisitor) {
			((CSharpPsiElementVisitor) visitor).visitCompilerVariable(this);
		} else {
			super.accept(visitor);
		}
	}

	@Override
	@NotNull
	public PsiElement getNameElement() {
		return findNotNullChildByType(CPsiTokens.IDENTIFIER);
	}
}
