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

import java.util.Set;

import javax.swing.Icon;

import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.CFileType;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.lang.psi.CPsiGenFile;
import org.napile.cpp4idea.lang.psi.CPsiRawFile;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;
import org.napile.cpp4idea.lang.psi.visitors.impl.CPsiGenFileVisitor;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @date 2:12/10.12.2011
 */
public class CPsiRawFileImpl extends PsiFileBase implements CPsiRawFile
{
	private static final String[] SOURCE_FILES = new String[] {"c", "cpp"};
	private boolean _isSourceFile;

	private CPsiGenFile _genFile;

	public CPsiRawFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, CLanguage.INSTANCE);

		VirtualFile virtualFile = viewProvider.getVirtualFile();

		_isSourceFile = ArrayUtils.contains(SOURCE_FILES, virtualFile.getExtension());
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
		return _isSourceFile;
	}

	@Override
	public void buildGen(@NotNull Set<String> list, @NotNull Set<PsiFile> includeFiles)
	{
		if(_genFile != null)
			return;

		includeFiles.add(getContainingFile());

		CPsiGenFileVisitor visitor = new CPsiGenFileVisitor(list, includeFiles);
		visitor.visitRawFile(this);

		_genFile = visitor.getGenFile();
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CPsiElementVisitor)
			((CPsiElementVisitor)visitor).visitRawFile(this);
		else
			super.accept(visitor);
	}

	@Override
	public Icon getElementIcon(int flag)
	{
		return isSourceFile() ? CIcons.SOURCE_FILE : CIcons.HEADER_FILE;
	}

	@Override
	@Nullable
	public CPsiGenFile getGenFile()
	{
		return _genFile;
	}
}
