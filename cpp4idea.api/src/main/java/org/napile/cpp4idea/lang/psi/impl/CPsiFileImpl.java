/*
 * Copyright 2010-2012 napile.org
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
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 15:41/29.12.12
 */
public class CPsiFileImpl extends CPsiElementBaseImpl implements CPsiFile
{
	private final CPsiSharpFile originalFile;

	public CPsiFileImpl(@NotNull ASTNode node)
	{
		super(node);
		originalFile = node.getUserData(C_SHARP_FILE);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CPsiElementVisitor)
			((CPsiElementVisitor) visitor).visitCFile(this);
		else
			visitor.visitElement(this);
	}

	@NotNull
	@Override
	public CPsiSharpFile getOriginalFile()
	{
		return originalFile;
	}
}
