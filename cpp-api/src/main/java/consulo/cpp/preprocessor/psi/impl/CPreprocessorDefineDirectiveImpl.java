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

import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.impl.CPsiElementBaseImpl;
import consulo.cpp.preprocessor.psi.CPsiCompilerVariable;
import consulo.cpp.preprocessor.psi.CPsiSharpDefineValue;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 7:23/11.12.2011
 */
public class CPreprocessorDefineDirectiveImpl extends CPsiElementBaseImpl implements CPreprocessorDefineDirective {
	public CPreprocessorDefineDirectiveImpl(@org.jetbrains.annotations.NotNull ASTNode node) {
		super(node);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof CPreprocessorElementVisitor) {
			((CPreprocessorElementVisitor) visitor).visitSDefine(this);
		} else {
			super.accept(visitor);
		}
	}

	@Override
	public CPsiCompilerVariable getVariable() {
		return findChildByClass(CPsiCompilerVariable.class);
	}

	@Override
	public CPsiSharpDefineValue getValue() {
		return findChildByClass(CPsiSharpDefineValue.class);
	}
}
