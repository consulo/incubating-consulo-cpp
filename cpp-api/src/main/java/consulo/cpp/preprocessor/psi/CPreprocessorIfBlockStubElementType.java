/*
 * Copyright 2013-2026 consulo.io
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
package consulo.cpp.preprocessor.psi;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorIfBlockImpl;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBlockStub;
import consulo.cpp.preprocessor.psi.stub.impl.CPreprocessorIfBlockStubImpl;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CPreprocessorIfBlockStubElementType
        extends IStubElementType<CPreprocessorIfBlockStub, CPreprocessorIfBlock> {

    public static final CPreprocessorIfBlockStubElementType INSTANCE = new CPreprocessorIfBlockStubElementType();

    private CPreprocessorIfBlockStubElementType() {
        super(CPreprocessorIfBlock.class.getSimpleName(), CPreprocessorLanguage.INSTANCE);
    }

    public PsiElement createPsiFromNode(@NotNull ASTNode node) {
        return new CPreprocessorIfBlockImpl(node);
    }

    @Override
    public @NotNull CPreprocessorIfBlock createPsi(@NotNull CPreprocessorIfBlockStub stub) {
        return new CPreprocessorIfBlockImpl(stub, this);
    }

    @Override
    public @NotNull CPreprocessorIfBlockStub createStub(@NotNull CPreprocessorIfBlock psi, StubElement parentStub) {
        CPreprocessorMacroReference variable = psi.getVariable();
        return new CPreprocessorIfBlockStubImpl(parentStub, this,
                variable == null ? null : variable.getText(),
                psi.isReverted());
    }

    @Override
    public @NotNull String getExternalId() {
        return "cpp.preprocessor.ifBlock";
    }

    @Override
    public void serialize(@NotNull CPreprocessorIfBlockStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getVariableName());
        dataStream.writeBoolean(stub.isReverted());
    }

    @Override
    public @NotNull CPreprocessorIfBlockStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        String name = dataStream.readNameString();
        boolean reverted = dataStream.readBoolean();
        return new CPreprocessorIfBlockStubImpl(parentStub, this, name, reverted);
    }

    @Override
    public void indexStub(@NotNull CPreprocessorIfBlockStub stub, @NotNull IndexSink sink) {
    }
}
