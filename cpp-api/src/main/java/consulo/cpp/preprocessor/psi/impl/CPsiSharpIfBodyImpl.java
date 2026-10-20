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

package consulo.cpp.preprocessor.psi.impl;

import consulo.language.psi.stub.IStubElementType;

import consulo.language.impl.psi.stub.StubBasedPsiElementBase;

import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBodyStub;

import consulo.cpp.preprocessor.psi.CPreprocessorIfBlock;
import consulo.cpp.preprocessor.psi.CPsiSharpIfBody;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorElementVisitor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import consulo.cpp.lang.psi.impl.CPsiElementBaseImpl;

/**
 * @author VISTALL
 * @date 14:06/29.12.12
 */
public class CPsiSharpIfBodyImpl extends StubBasedPsiElementBase<CPreprocessorIfBodyStub> implements CPsiSharpIfBody
{
	public CPsiSharpIfBodyImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public CPsiSharpIfBodyImpl(@NotNull CPreprocessorIfBodyStub stub, @NotNull IStubElementType<?, ?> elementType)
	{
		super(stub, elementType);
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ": " + getText();
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof CPreprocessorElementVisitor)
		{
			((CPreprocessorElementVisitor) visitor).visitSIfBody(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public boolean isElseBody()
	{
		CPreprocessorIfBodyStub stub = getStub();
		if(stub != null)
		{
			return stub.isElseBody();
		}
		return getIfDef().getElseBody() == this;
	}

	@Override
	public CPreprocessorIfBlock getIfDef()
	{
		return (CPreprocessorIfBlock) getParent();
	}
}
