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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import consulo.cpp.preprocessor.psi.CPreprocessorMacroReference;
import consulo.cpp.preprocessor.psi.CPreprocessorTokenTypes;
import consulo.cpp.preprocessor.psi.CPsiSharpIfBody;
import consulo.cpp.preprocessor.psi.CPsiSharpIfDef;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.impl.CPsiElementBaseImpl;

/**
 * @author VISTALL
 * @date 14:07/11.12.2011
 */
public class CPsiSharpIfDefImpl extends CPsiElementBaseImpl implements CPsiSharpIfDef {
	public CPsiSharpIfDefImpl(@org.jetbrains.annotations.NotNull ASTNode node) {
		super(node);
	}

	@Override
	public CPreprocessorMacroReference getVariable() {
		return findChildByClass(CPreprocessorMacroReference.class);
	}

	@Nullable
	@Override
	public CPsiSharpIfBody getBody() {
		return findChildByClass(CPsiSharpIfBody.class);
	}

	@Nullable
	@Override
	public PsiElement getElseKeyword() {
		return findChildByType(CPreprocessorTokenTypes.S_ELSE_KEYWORD);
	}

	@Nullable
	@Override
	public CPsiSharpIfBody getElseBody() {
		CPsiSharpIfBody body = getBody();
		if (body == null) {
			return null;
		}

		PsiElement element = body.getNextSibling();
		while (element != null) {
			if (element instanceof CPsiSharpIfBody) {
				return (CPsiSharpIfBody) element;
			}
			element = element.getNextSibling();
		}
		return null;
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof CPreprocessorElementVisitor) {
			((CPreprocessorElementVisitor) visitor).visitSIfDef(this);
		} else {
			super.accept(visitor);
		}
	}

	@Override
	public boolean isReverted() {
		return findChildByType(CPreprocessorTokenTypes.S_IFNDEF_KEYWORD) != null;
	}
}
