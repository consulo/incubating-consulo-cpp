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

package org.napile.cpp4idea.lang.psiInitial.impl;

import javax.swing.Icon;

import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CFileType;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;
import org.napile.cpp4idea.lang.psiInitial.visitors.CSharpPsiElementVisitor;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 2:12/10.12.2011
 */
public class CPsiSharpFileImpl extends PsiFileBase implements CPsiSharpFile
{

	private static final String[] SOURCE_FILES = new String[] {"c", "cpp"};
	private boolean isSourceFile;

	public CPsiSharpFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, CLanguage.INSTANCE);

		VirtualFile virtualFile = viewProvider.getVirtualFile();

		isSourceFile = ArrayUtils.contains(SOURCE_FILES, virtualFile.getExtension());
	}

	@Override
	public void clearCaches()
	{
		super.clearCaches();

		putUserData(AFTER_PROCESSED_FILE, null);
	}

	@NotNull
	@Override
	public FileType getFileType()
	{
		return CFileType.INSTANCE;
	}

	@Override
	public boolean isSourceFile()
	{
		return isSourceFile;
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CSharpPsiElementVisitor)
			((CSharpPsiElementVisitor)visitor).visitSFile(this);
		else
			super.accept(visitor);
	}

	@Override
	public Icon getElementIcon(int flag)
	{
		return isSourceFile() ? CIcons.SOURCE_FILE : CIcons.HEADER_FILE;
	}
}
