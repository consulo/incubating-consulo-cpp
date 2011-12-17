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

package org.napile.cpp4idea.annotator.parsing;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.lang.psi.CPsiRawFile;
import org.napile.cpp4idea.lang.psi.impl.wrapper.CPsiDefineWrapper;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;
import org.napile.cpp4idea.lang.psi.visitors.CPsiRecursiveElementVisitor;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 16:27/15.12.2011
 * 2nd annotator - generate CPsiGenFile
 */
public class CSecondStepAnnotator implements Annotator
{
	private static class CPsiRecursiveVisitorImpl extends CPsiRecursiveElementVisitor
	{
		private AnnotationHolder _annotationHolder;

		private CPsiRecursiveVisitorImpl(AnnotationHolder annotationHolder)
		{
			_annotationHolder = annotationHolder;
		}

		@Override
		public void visitElement(PsiElement element)
		{
			if(element instanceof CPsiDefineWrapper)
			{
				CPsiDefineWrapper wrapper = (CPsiDefineWrapper)element;
				PsiElement targetElement = wrapper.getTarget();

				TextAttributesKey[] attributesKeys = CSyntaxHighlighter.ATTRIBUTES.get(targetElement.getNode().getElementType());
				if(attributesKeys != null)
				{
					Annotation annotator = _annotationHolder.createInfoAnnotation(targetElement, null);

					annotator.setTextAttributes(attributesKeys[wrapper.isDefined() ? 0 : 1]);
				}
			}

			super.visitElement(element);
		}
	}

	@Override
	public void annotate(@NotNull PsiElement element, final @NotNull AnnotationHolder holder)
	{
		if(!(element instanceof CPsiRawFile))
			return;

		CPsiRawFile file = (CPsiRawFile)element;

		file.buildGen(new HashSet<String>());

		CPsiElementVisitor visitor = new CPsiRecursiveVisitorImpl(holder);

		file.getGenFile().accept(visitor);
	}
}
