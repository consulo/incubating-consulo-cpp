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

import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.cpp.preprocessor.psi.CPsiSharpDefineValue;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorDefineStub;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorElementVisitor;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.stub.StubBasedPsiElementBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 7:23/11.12.2011
 */
public class CPreprocessorDefineDirectiveImpl
        extends StubBasedPsiElementBase<CPreprocessorDefineStub>
        implements CPreprocessorDefineDirective
{
    /** AST mode - used by the parser. */
    public CPreprocessorDefineDirectiveImpl(@NotNull ASTNode node)
    {
        super(node);
    }

    /** Stub mode - used when the file is accessed via the stub index. */
    public CPreprocessorDefineDirectiveImpl(@NotNull CPreprocessorDefineStub stub,
                                             @NotNull IStubElementType<?, ?> elementType)
    {
        super(stub, elementType);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor)
    {
        if(visitor instanceof CPreprocessorElementVisitor)
        {
            ((CPreprocessorElementVisitor) visitor).visitSDefine(this);
        }
        else
        {
            super.accept(visitor);
        }
    }

    @Override
    public String getName()
    {
        // Fast path: read from stub if available (avoids AST loading)
        CPreprocessorDefineStub stub = getStub();
        if (stub != null)
        {
            return stub.getName();
        }
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @Override
    public CPsiSharpDefineValue getValue()
    {
        return findChildByClass(CPsiSharpDefineValue.class);
    }

    @Override
    public int getTextOffset()
    {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? super.getTextOffset() : nameIdentifier.getTextOffset();
    }

    @Override
    @Nullable
    public PsiElement getNameIdentifier()
    {
        return findChildByType(CPsiTokens.IDENTIFIER);
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException
    {
        return null;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + ": " + getText();
    }
}
