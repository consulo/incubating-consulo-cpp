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

package consulo.cpp.preprocessor.psi.impl;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.CPreprocessorFile;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorElementVisitor;
import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.language.psi.PsiElementVisitor;
import consulo.virtualFileSystem.fileType.FileType;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 2:12/10.12.2011
 */
public class CPreprocessorFileImpl extends PsiFileBase implements CPreprocessorFile
{
	public CPreprocessorFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, CPreprocessorLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType()
	{
		return getViewProvider().getFileType();
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CPreprocessorElementVisitor)
		{
			((CPreprocessorElementVisitor) visitor).visitPreprocessorFile(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public String toString()
	{
		return "CPreprocessorFile:" + getViewProvider().getVirtualFile().getName();
	}
}
