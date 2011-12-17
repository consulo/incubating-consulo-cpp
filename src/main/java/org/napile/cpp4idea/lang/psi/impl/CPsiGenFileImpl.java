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

package org.napile.cpp4idea.lang.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiGenFile;
import org.napile.cpp4idea.lang.psi.CPsiRawFile;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiElementBase;
import com.intellij.util.IncorrectOperationException;

/**
 * @author VISTALL
 * @date 16:34/15.12.2011
 */
public class CPsiGenFileImpl extends PsiElementBase implements CPsiGenFile
{
	private CPsiRawFile _binaryFile;
	private PsiElement[] _children;

	public CPsiGenFileImpl(CPsiRawFile binaryFile, PsiElement[] children)
	{
		_binaryFile = binaryFile;
		_children = children;
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CPsiElementVisitor)
			((CPsiElementVisitor)visitor).visitGenFile(this);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void acceptChildren(@NotNull PsiElementVisitor visitor)
	{
		for(PsiElement child : _children)
			child.accept(visitor);
	}

	@NotNull
	@Override
	public Language getLanguage()
	{
		return _binaryFile.getLanguage();
	}

	@Override
	public PsiManager getManager()
	{
		return _binaryFile.getManager();
	}

	@NotNull
	@Override
	public PsiElement[] getChildren()
	{
		return _children;
	}

	@Override
	public PsiElement getParent()
	{
		return _binaryFile;
	}

	@Override
	public TextRange getTextRange()
	{
		return null;
	}

	@Override
	public int getStartOffsetInParent()
	{
		return 0;
	}

	@Override
	public int getTextLength()
	{
		return 0;
	}

	@Override
	public PsiElement findElementAt(int offset)
	{
		return null;
	}

	@Override
	public int getTextOffset()
	{
		return 0;
	}

	@Override
	public String getText()
	{
		return null;
	}

	@NotNull
	@Override
	public char[] textToCharArray()
	{
		return new char[0];
	}

	@Override
	public PsiElement copy()
	{
		return null;
	}

	@Override
	public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException
	{

	}

	@Override
	public void delete() throws IncorrectOperationException
	{

	}

	@Override
	public void checkDelete() throws IncorrectOperationException
	{

	}

	@Override
	public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public ASTNode getNode()
	{
		return null;
	}
}
