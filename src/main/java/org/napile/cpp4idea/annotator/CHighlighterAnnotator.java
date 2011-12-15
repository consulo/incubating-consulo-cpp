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

package org.napile.cpp4idea.annotator;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import org.napile.cpp4idea.lang.psi.visitors.CPsiRecursiveVisitor;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
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

		cElement.accept(this, null);
	}

/*	@Override
	public void visitCompilerVariable(CPsiCompilerVariable element)
	{
		Annotation annotation = _annotationHolder.createInfoAnnotation(element, null);

		annotation.setTextAttributes(CSyntaxHighlighter.COMPILER_VARIABLE);
	}

	@Override
	public void visitInclude(CPsiInclude include)
	{
		PsiElement element = include.getIncludeElement();
		if(element == null)
			return;

		PsiFile psiFile = include.getContainingFile();
		VirtualFile virtualFile = psiFile.getVirtualFile();
		if(virtualFile == null)
			return;

		VirtualFile parentDir = virtualFile.getParent();
		if(parentDir == null)
			return;

		String includeName = include.getIncludeName();
		VirtualFile includeFile = parentDir.findFileByRelativePath(FileUtil.toSystemIndependentName(includeName));
		if(includeFile == null)
			_annotationHolder.createErrorAnnotation(element, CBundle.message("not.find.header", includeName));
	}

	@Override
	public void visitEnum(CPsiEnum psiEnum)
	{
		CPsiEnumConstant[] constants = psiEnum.getConstants();

		for(CPsiEnumConstant constant : constants)
			for(CPsiEnumConstant $constant : constants)
				if(constant != $constant && constant.getNameElement().getText().equalsIgnoreCase($constant.getNameElement().getText()))
					_annotationHolder.createErrorAnnotation(constant, CBundle.message("duplicate.enum.constant"));
	}

	@Override
	public void visitEnumConstant(CPsiEnumConstant include)
	{
		PsiElement element = include.getNameElement();
		if(element == null)
			return;

		Annotation annotation = _annotationHolder.createInfoAnnotation(element, null);

		annotation.setTextAttributes(CSyntaxHighlighter.CONSTANT);
	}

	@Override
	public void visitIndependInclude(CPsiIndependInclude include)
	{
		PsiElement element = include.getIncludeElement();
		if(element == null)
			return;

		Annotation annotation = _annotationHolder.createInfoAnnotation(element, null);

		annotation.setTextAttributes(CSyntaxHighlighter.STRING);
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
		int startOffset = holder.getTextOffset();
		int endOffset = startOffset + holder.getTextLength();

		Annotation annotation = _annotationHolder.createInfoAnnotation(holder, "null");
		annotation.setTextAttributes(CSyntaxHighlighter.LINE_COMMENT);
	}    */
}
