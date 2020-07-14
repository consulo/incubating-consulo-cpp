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

package org.napile.cpp4idea.editor;

import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.lang.parser.parsingMain.builder.CMainPsiBuilder;
import org.napile.cpp4idea.lang.psi.CPsiEnumConstant;
import org.napile.cpp4idea.lang.psi.visitors.CPsiRecursiveElementVisitor;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;

/**
 * @author VISTALL
 * @date 11:17/02.01.13
 */
public class AfterHighlighterVisitor extends CPsiRecursiveElementVisitor {
	private final AnnotationHolder holder;

	public AfterHighlighterVisitor(AnnotationHolder holder) {
		this.holder = holder;
	}

	@Override
	public void visitEnumConstant(CPsiEnumConstant element) {
		PsiElement nameElement = element.getNameElement();
		if (nameElement != null) {
			HighlightUtil.highlightOriginalElement(nameElement, holder, CSyntaxHighlighter.CONSTANT);
		}
	}

	@Override
	public void visitErrorElement(PsiErrorElement element) {
		TextRange textRange = element.getUserData(CMainPsiBuilder.ORIGINAL_TEXT_RANGE);

		holder.createErrorAnnotation(textRange, element.getErrorDescription());

		super.visitErrorElement(element);
	}
}
