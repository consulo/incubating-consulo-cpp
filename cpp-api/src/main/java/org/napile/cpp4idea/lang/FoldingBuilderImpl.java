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

package org.napile.cpp4idea.lang;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.cpp.preprocessor.psi.CPreprocessorIfBlock;
import consulo.document.Document;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.editor.folding.FoldingBuilder;
import consulo.language.editor.folding.FoldingDescriptor;
import consulo.language.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psi.CPsiImplementingMethod;
import org.napile.cpp4idea.lang.psi.visitors.CPsiRecursiveElementVisitor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @date 14:17/16.12.2011
 */
@ExtensionImpl
public class FoldingBuilderImpl implements FoldingBuilder, DumbAware
{
	private static final FoldingDescriptor[] EMPTY = new FoldingDescriptor[0];

	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document)
	{
		PsiElement element = node.getPsi();
		if(element instanceof CPsiFile)
		{
			CPsiFile psiFile = (CPsiFile) element;

			final List<FoldingDescriptor> list = new ArrayList<>();

			//			element.accept(new CPreprocessorRecursiveElementVisitor() {
			//				@Override
			//				public void visitSIfDef(CPsiSharpIfDef element) {
			//					if (element.getVariable() != null) {
			//						list.add(new FoldingDescriptor(element, element.getTextRange()));
			//					}
			//					super.visitSIfDef(element);
			//				}
			//			});

			psiFile.accept(new CPsiRecursiveElementVisitor()
			{
				@Override
				public void visitImplementingMethod(CPsiImplementingMethod element)
				{
					//list.add(new FoldingDescriptor(element, HighlightUtil.findOriginalRange(element)));

					super.visitImplementingMethod(element);
				}
			});

			return list.toArray(new FoldingDescriptor[list.size()]);
		}
		else
		{
			return EMPTY;
		}
	}

	@Override
	public String getPlaceholderText(@NotNull ASTNode node)
	{
		PsiElement element = node.getPsi();
		if(element instanceof CPreprocessorIfBlock)
		{
			return (((CPreprocessorIfBlock) element).isReverted() ? "#ifndef " : "#ifdef ") + ((CPreprocessorIfBlock) element).getVariable().getText() + "\n";
		}
		else if(element instanceof CPsiImplementingMethod)
		{
			return "{....}";
		}

		throw new IllegalArgumentException();
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node)
	{
		PsiElement element = node.getPsi();

		return false;
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CLanguage.INSTANCE;
	}
}
