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
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psi.CPsiInclude;
import org.napile.cpp4idea.lang.psi.CPsiRawFile;
import org.napile.cpp4idea.lang.psi.CPsiSharpDefine;
import org.napile.cpp4idea.lang.psi.CPsiSharpVariableChecker;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;
import org.napile.cpp4idea.lang.psi.visitors.CPsiRecursiveElementVisitor;
import org.napile.cpp4idea.util.CPsiUtil;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @date 16:27/15.12.2011
 */
public class CSecondStepAnnotator implements Annotator
{
	private static class CPsiRecursiveVisitorImpl extends CPsiRecursiveElementVisitor
	{
		private AnnotationHolder _annotationHolder;
		private Set<String> _defineList;

		private CPsiRecursiveVisitorImpl(AnnotationHolder annotationHolder, Set<String> list)
		{
			_annotationHolder = annotationHolder;

			_defineList = list;
		}

		@Override
		public void visitElement(PsiElement element)
		{
			TextAttributesKey[] attributesKeys = CSyntaxHighlighter.ATTRIBUTES.get(element.getNode().getElementType());
			if(attributesKeys != null)
			{
				Annotation annotator = _annotationHolder.createInfoAnnotation(element, null);

				int val = isEqual(CPsiUtil.getLastParentChecker(element, null)) && isEqual(CPsiUtil.getFirstParentChecker(element)) ? 0 : 1;

				annotator.setTextAttributes(attributesKeys[val]);
			}

			super.visitElement(element);
		}

		@Override
		public void visitSDefine(CPsiSharpDefine element)
		{
			CPsiCompilerVariable var = element.getVariable();
			if(var != null)
				_defineList.add(var.getText());

			super.visitSDefine(element);
		}

		@Override
		public void visitCompilerVariable(CPsiCompilerVariable variable)
		{
			Annotation annotator = _annotationHolder.createInfoAnnotation(variable, null);
			annotator.setTextAttributes(getTextAttributesKey(variable, CSyntaxHighlighter.COMPILER_VARIABLE_CACHE));

			super.visitCompilerVariable(variable);
		}

		@Override
		public void visitSInclude(CPsiInclude include)
		{
			PsiElement element = include.getIncludeElement();
			if(element != null)
			{
				CPsiSharpVariableChecker checker = CPsiUtil.getFirstParentChecker(element);
				if(checker != null)
				{
					CPsiCompilerVariable var = checker.getVariable();
					if(var != null && _defineList.contains(var.getText()) == checker.getEqualValue())
					{
						PsiFile psiFile = include.getContainingFile();
						VirtualFile virtualFile = psiFile.getVirtualFile();
						if(virtualFile != null)
						{
							VirtualFile parentDir = virtualFile.getParent();
							if(parentDir != null)
							{
								String includeName = include.getIncludeName();
								VirtualFile includeFile = parentDir.findFileByRelativePath(FileUtil.toSystemIndependentName(includeName));
								if(includeFile == null)
									_annotationHolder.createErrorAnnotation(element, CBundle.message("not.find.header", includeName));
								else
								{
									PsiFile file = include.getManager().findFile(includeFile);
									if(file instanceof CPsiRawFile)
										visitRawFile((CPsiRawFile)file);
								}
							}
						}
					}
				}
			}
			super.visitSInclude(include);
		}

		private TextAttributesKey getTextAttributesKey(final @NotNull PsiElement element, TextAttributesKey[] attributes)
		{
			return attributes[isEqual(CPsiUtil.getLastParentChecker(element, null)) ? 0 : 1];
		}

		private boolean isEqual(CPsiSharpVariableChecker checker)
		{
			if(checker == null)
				return true;

			CPsiCompilerVariable var = checker.getVariable();

			return var != null && _defineList.contains(var.getText()) == checker.getEqualValue();
		}
	}

	@Override
	public void annotate(@NotNull PsiElement element, final @NotNull AnnotationHolder holder)
	{
		if(!(element instanceof CPsiRawFile))
			return;

		final Set<String> defines = new HashSet<String> ();

		// build define
		CPsiElementVisitor visitor = new CPsiRecursiveVisitorImpl(holder, defines);

		visitor.visitRawFile((CPsiRawFile) element);
	}
}
