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

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.lang.parser.parsingMain.builder.CMainPsiBuilder;
import org.napile.cpp4idea.lang.psiInitial.CPsiCompilerVariable;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 11:30/02.01.13
 */
public class HighlightUtil {
	public static void highlight(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		if (element instanceof CPsiCompilerVariable)
			highlightElement(element, holder, CSyntaxHighlighter.COMPILER_VARIABLE);
	}

	public static void highlightOriginalElement(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
		PsiElement originalElement = element.getUserData(CMainPsiBuilder.ORIGINAL_SINGLE_ELEMENT);
		if (originalElement != null)
			holder.createInfoAnnotation(originalElement, null).setTextAttributes(key);
		else
			throw new UnsupportedOperationException("Unknown how highlight element : " + element);
	}

	public static void highlightElement(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
		holder.createInfoAnnotation(element, null).setTextAttributes(key);
	}
}
