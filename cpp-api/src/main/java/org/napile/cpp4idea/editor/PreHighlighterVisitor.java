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
import org.napile.cpp4idea.lang.preprocessor.CPreprocessor;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psiInitial.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpIfBody;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpIfDef;
import org.napile.cpp4idea.lang.psiInitial.visitors.CSharpPsiRecursiveElementVisitor;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 11:07/02.01.13
 */
public class PreHighlighterVisitor extends CSharpPsiRecursiveElementVisitor
{
	//FIXME [VISTALL] better
	private static final CSyntaxHighlighter HIGHLIGHTER = new CSyntaxHighlighter(null, null);

	private final AnnotationHolder holder;
	private final CPsiFile afterFile;
	private final CPsiSharpFile preFile;

	public PreHighlighterVisitor(AnnotationHolder holder, CPsiSharpFile element)
	{
		this.holder = holder;

		preFile = element;
		afterFile = CPreprocessor.getAfterProcessedFile(element);
	}

	public void start()
	{
		if(afterFile == null)
			return;

		AfterHighlighterVisitor afterHighlighterVisitor = new AfterHighlighterVisitor(holder);

		preFile.accept(this);

		afterFile.accept(afterHighlighterVisitor);
	}

	@Override
	public void visitCompilerVariable(CPsiCompilerVariable element)
	{
		HighlightUtil.highlight(element, holder);

		super.visitCompilerVariable(element);
	}

	@Override
	public void visitSIfBody(CPsiSharpIfBody element)
	{
		if(element.getTextLength() == 0)
			return;

		CPsiSharpIfDef def = element.getIfDef();

		boolean defined = element.getUserData(CPreprocessor.ACTIVE_BLOCK) == Boolean.TRUE;

		if(!defined)
		{
			//holder.createWarningAnnotation(element, "Not active block");

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
		else
			super.visitSIfBody(element);
	}
}
