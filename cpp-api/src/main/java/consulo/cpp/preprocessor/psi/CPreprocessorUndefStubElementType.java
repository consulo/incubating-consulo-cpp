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
import consulo.cpp.preprocessor.psi.impl.CPreprocessorUndefDirectiveImpl;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorUndefStub;
import consulo.cpp.preprocessor.psi.stub.impl.CPreprocessorUndefStubImpl;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CPreprocessorUndefStubElementType
        extends IStubElementType<CPreprocessorUndefStub, CPreprocessorUndefDirective> {

    public static final CPreprocessorUndefStubElementType INSTANCE = new CPreprocessorUndefStubElementType();

    private CPreprocessorUndefStubElementType() {
        super(CPreprocessorUndefDirective.class.getSimpleName(), CPreprocessorLanguage.INSTANCE);
    }

    public PsiElement createPsiFromNode(@NotNull ASTNode node) {
        return new CPreprocessorUndefDirectiveImpl(node);
    }

    @Override
    public @NotNull CPreprocessorUndefDirective createPsi(@NotNull CPreprocessorUndefStub stub) {
        return new CPreprocessorUndefDirectiveImpl(stub, this);
    }

    @Override
    public @NotNull CPreprocessorUndefStub createStub(@NotNull CPreprocessorUndefDirective psi, StubElement parentStub) {
        return new CPreprocessorUndefStubImpl(parentStub, this, psi.getName());
    }

    @Override
    public @NotNull String getExternalId() {
        return "cpp.preprocessor.undef";
    }

    @Override
    public void serialize(@NotNull CPreprocessorUndefStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
    }

    @Override
    public @NotNull CPreprocessorUndefStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new CPreprocessorUndefStubImpl(parentStub, this, dataStream.readNameString());
    }

    @Override
    public void indexStub(@NotNull CPreprocessorUndefStub stub, @NotNull IndexSink sink) {
    }
}
