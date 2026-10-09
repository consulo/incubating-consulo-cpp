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
import consulo.cpp.preprocessor.psi.impl.CPsiSharpIfBodyImpl;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBodyStub;
import consulo.cpp.preprocessor.psi.stub.impl.CPreprocessorIfBodyStubImpl;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CPreprocessorIfBodyStubElementType
        extends IStubElementType<CPreprocessorIfBodyStub, CPsiSharpIfBody> {

    public static final CPreprocessorIfBodyStubElementType INSTANCE = new CPreprocessorIfBodyStubElementType();

    private CPreprocessorIfBodyStubElementType() {
        super(CPsiSharpIfBody.class.getSimpleName(), CPreprocessorLanguage.INSTANCE);
    }

    public PsiElement createPsiFromNode(@NotNull ASTNode node) {
        return new CPsiSharpIfBodyImpl(node);
    }

    @Override
    public @NotNull CPsiSharpIfBody createPsi(@NotNull CPreprocessorIfBodyStub stub) {
        return new CPsiSharpIfBodyImpl(stub, this);
    }

    @Override
    public @NotNull CPreprocessorIfBodyStub createStub(@NotNull CPsiSharpIfBody psi, StubElement parentStub) {
        return new CPreprocessorIfBodyStubImpl(parentStub, this, psi.isElseBody());
    }

    @Override
    public @NotNull String getExternalId() {
        return "cpp.preprocessor.ifBody";
    }

    @Override
    public void serialize(@NotNull CPreprocessorIfBodyStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeBoolean(stub.isElseBody());
    }

    @Override
    public @NotNull CPreprocessorIfBodyStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new CPreprocessorIfBodyStubImpl(parentStub, this, dataStream.readBoolean());
    }

    @Override
    public void indexStub(@NotNull CPreprocessorIfBodyStub stub, @NotNull IndexSink sink) {
    }
}
