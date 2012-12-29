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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpIfDef;
import org.napile.cpp4idea.lang.psiInitial.visitors.CSharpPsiRecursiveElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 14:17/16.12.2011
 */
public class FoldingBuilderImpl implements FoldingBuilder, DumbAware
{
	private static final FoldingDescriptor[] EMPTY = new FoldingDescriptor[0];

	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document)
	{
		PsiElement element = node.getPsi();
		if(element instanceof CPsiSharpFile)
		{
			final List<FoldingDescriptor> list = new ArrayList<FoldingDescriptor>();

			CSharpPsiRecursiveElementVisitor visitor = new CSharpPsiRecursiveElementVisitor()
			{
				@Override
				public void visitSIfDef(CPsiSharpIfDef element)
				{
					if(element.getVariable() != null)
						list.add(new FoldingDescriptor(element, element.getTextRange()));
					super.visitSIfDef(element);
				}
			};

			visitor.visitSFile((CPsiSharpFile) element);

			return list.toArray(new FoldingDescriptor[list.size()]);
		}
		else
			return EMPTY;
	}

	@Override
	public String getPlaceholderText(@NotNull ASTNode node)
	{
		PsiElement element = node.getPsi();
		if(element instanceof CPsiSharpIfDef)
			return (((CPsiSharpIfDef) element).isReverted() ? "#ifndef " : "#ifdef ") + ((CPsiSharpIfDef) element).getVariable().getText();
		//else if(element instanceof CPsiImplementingMethod)
		//	return "{...}";

		throw new IllegalArgumentException();
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node)
	{
		return false;
	}
}
