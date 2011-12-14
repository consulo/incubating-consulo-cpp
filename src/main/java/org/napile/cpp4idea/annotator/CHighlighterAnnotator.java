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
 *    limitations under the License.
 */

package org.napile.cpp4idea.annotator;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariableHolder;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import org.napile.cpp4idea.lang.psi.CPsiIfDefHolder;
import org.napile.cpp4idea.lang.psi.CPsiIfNotDefHolder;
import org.napile.cpp4idea.lang.psi.CPsiIndependInclude;
import org.napile.cpp4idea.lang.psi.visitors.CPsiRecursiveVisitor;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 17:41/11.12.2011
 */
public class CHighlighterAnnotator extends CPsiRecursiveVisitor implements Annotator
{
	private AnnotationHolder _annotationHolder;

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
	{
		if(!(element instanceof CPsiElement))
			return;
		_annotationHolder = holder;

		CPsiElement cElement = (CPsiElement)element;

		cElement.accept(this);
	}

	@Override
	public void visitCompilerVariable(CPsiCompilerVariable element)
	{
		Annotation annotation = _annotationHolder.createInfoAnnotation(element, null);

		annotation.setTextAttributes(CSyntaxHighlighter.COMPILER_VARIABLE);
	}

	@Override
	public void visitIndependInclude(CPsiIndependInclude include)
	{
		PsiElement element = include.getIncludeElement();
		if(element == null)
			return;

		Annotation annotation = _annotationHolder.createInfoAnnotation(element, null);

		annotation.setTextAttributes(SyntaxHighlighterColors.STRING);
	}

	@Override
	public void visitIfDefHolder(CPsiIfDefHolder element)
	{
		paintIfCompilerBlock(element, true);
	}

	@Override
	public void visitIfNotDefHolder(CPsiIfNotDefHolder element)
	{
		paintIfCompilerBlock(element, false);
	}

	private void paintIfCompilerBlock(CPsiCompilerVariableHolder holder, boolean val)
	{

	}
}
