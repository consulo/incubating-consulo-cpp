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

package org.napile.cpp4idea.lang.psi.visitors.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.napile.cpp4idea.lang.psi.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import org.napile.cpp4idea.lang.psi.CPsiGenFile;
import org.napile.cpp4idea.lang.psi.CPsiInclude;
import org.napile.cpp4idea.lang.psi.CPsiRawFile;
import org.napile.cpp4idea.lang.psi.CPsiSharpDefine;
import org.napile.cpp4idea.lang.psi.impl.CPsiGenFileImpl;
import org.napile.cpp4idea.lang.psi.impl.wrapper.PsiElementWrapper;
import org.napile.cpp4idea.lang.psi.visitors.CPsiRecursiveElementVisitor;
import org.napile.cpp4idea.util.CPsiUtil;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @date 17:35/16.12.2011
 */
public class CPsiGenFileVisitor extends CPsiRecursiveElementVisitor
{
	private List<PsiElement> _elements;

	private CPsiRawFile _rawFile;

	private Set<String> _vars;
	private Set<PsiFile> _fileList;

	public CPsiGenFileVisitor(Set<String> vars, Set<PsiFile> fileList)
	{
		_vars = vars;
		_fileList = fileList;
	}

	@Override
	public void visitElement(PsiElement element)
	{
		if(!(element instanceof CPsiElement))
			_elements.add(new PsiElementWrapper(element, CPsiUtil.isBlockDefined(element, _vars)));

		super.visitElement(element);
	}

	@Override
	public void visitRawFile(CPsiRawFile rawFile)
	{
		_rawFile = rawFile;
		_elements = new ArrayList<PsiElement>(rawFile.getChildren().length);

		rawFile.acceptChildren(this);
	}

	@Override
	public void visitSDefine(CPsiSharpDefine element)
	{
		CPsiCompilerVariable var = element.getVariable();
		if(var != null)
			_vars.add(var.getText());

		super.visitSDefine(element);
	}

	@Override
	public void visitSInclude(CPsiInclude include)
	{
		PsiElement element = include.getIncludeElement();
		if(element != null)
		{
			if(CPsiUtil.isBlockDefined(include, _vars))
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
						if(includeFile != null)
						{
							PsiFile file = include.getManager().findFile(includeFile);
							if(file instanceof CPsiRawFile)
							{
								CPsiRawFile rawFile = (CPsiRawFile)file;

								rawFile.buildGen(_vars, _fileList);
							}
						}
					}
				}
			}
		}
		super.visitElement(include);
	}

	public CPsiGenFile getGenFile()
	{
		return new CPsiGenFileImpl(_rawFile, _rawFile.getViewProvider());
	}
}
