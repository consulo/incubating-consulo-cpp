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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.util.PsiTreeUtil;
import org.napile.cpp4idea.lang.parser.parsingMain.builder.CMainPsiBuilder;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;

/**
 * @author VISTALL
 * @date 22:56/10.12.2011
 */
public class CPsiElementBaseImpl extends ASTWrapperPsiElement implements CPsiElement {
	public CPsiElementBaseImpl(@org.jetbrains.annotations.NotNull ASTNode node) {
		super(node);
	}

	@Override
	public PsiElement getOriginalElement() {
		return getUserData(CMainPsiBuilder.ORIGINAL_SINGLE_ELEMENT);
	}

	@Override
	public PsiManagerEx getManager() {
		return super.getManager();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getText();
	}

	@Override
	public CPsiFile getCFile() {
		return PsiTreeUtil.getParentOfType(this, CPsiFile.class);
	}

	@Override
	public CPsiSharpFile getSharpCFile() {
		CPsiFile psiFile = getCFile();
		return psiFile == null ? null : psiFile.getSharpCFile();
	}
}
