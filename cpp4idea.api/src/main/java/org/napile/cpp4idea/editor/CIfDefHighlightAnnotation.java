/*
 * Copyright 2010-2012 napile.org
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
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpIfDef;
import org.napile.cpp4idea.lang.psiInitial.visitors.CSharpPsiRecursiveElementVisitor;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 20:05/29.12.12
 */
public class CIfDefHighlightAnnotation implements Annotator
{
	private static final CSyntaxHighlighter HIGHLIGHTER = new CSyntaxHighlighter(null, null);

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull final AnnotationHolder holder)
	{
		if(element instanceof CPsiSharpIfDef)
		{
			boolean defined = element.getUserData(CProcessAnnotator.ACTIVE_BLOCK) == Boolean.TRUE;
			if(!defined)
			{
				holder.createWarningAnnotation(element, "Not active block");

				element.accept(new CSharpPsiRecursiveElementVisitor()
				{
					@Override
					public void visitElement(PsiElement element)
					{
						TextAttributesKey[] textAttributesKeys = HIGHLIGHTER.getAttributes(element.getNode().getElementType());
						holder.createInfoAnnotation(element, null).setTextAttributes(textAttributesKeys != null ? textAttributesKeys[1] : CSyntaxHighlighter.LIGHT_IDENTIFIER);
						super.visitElement(element);
					}
				});
			}
		}
	}
}
