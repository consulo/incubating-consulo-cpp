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
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.lang.psi.CPsiBinaryFile;
import org.napile.cpp4idea.lang.psi.visitors.impl.CPsiVisitorNewImpl;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 16:27/15.12.2011
 */
public class CSecondStepAnnotator implements Annotator
{
	private static class CPsiRecursiveVisitorImpl extends CPsiVisitorNewImpl
	{
		private AnnotationHolder _annotationHolder;
		private Set<String> _defineList;

		private CPsiRecursiveVisitorImpl(AnnotationHolder annotationHolder, Set<String> list)
		{
			_annotationHolder = annotationHolder;

			_defineList = list;
		}

		@Override
		public void visitElement(PsiElement element, boolean defined)
		{
			TextAttributesKey[] attributesKeys = CSyntaxHighlighter.ATTRIBUTES.get(element.getNode().getElementType());
			if(attributesKeys != null)
			{
				Annotation annotator = _annotationHolder.createInfoAnnotation(element, null);
				annotator.setTextAttributes(attributesKeys[defined ? 0 : 1]);
			}
			super.visitElement(element, defined);
		}
	
		/*@Override
		public void visitDefine(CPsiDefine element, CPsiCompilerVariableHolder variableHolder)
		{
			CPsiCompilerVariable var = element.getVariable();
			if(var != null)
				_defineList.add(var.getText());

			super.visitDefine(element, variableHolder);
		}
           */

		//@Override
		//public void visitInclude(CPsiInclude include, CPsiCompilerVariableHolder variableHolder)
		//{
			/*PsiElement element = include.getIncludeElement();
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
			else
			{
				PsiFile file = include.getManager().findFile(virtualFile);
				if(!(file instanceof CPsiBinaryFile))
					visitFile((CPsiBinaryFile)file);
			}  */
		//}

		/*private boolean isDefined(CPsiCompilerVariableHolder holder)
		{
			if(holder == null)
				return true;

			CPsiCompilerVariable var = holder.getVariable();
			return var != null && _defineList.contains(var.getText());
		} */
	}

	@Override
	public void annotate(@NotNull PsiElement element, final @NotNull AnnotationHolder holder)
	{
		if(!(element instanceof CPsiBinaryFile))
			return;

		final Set<String> defines = new HashSet<String> ();

		// build define
		CPsiVisitorNewImpl visitor = new CPsiRecursiveVisitorImpl(holder, defines);

		visitor.visitBinaryFile((CPsiBinaryFile)element, true);
	}
}
