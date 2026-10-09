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
package consulo.cpp.preprocessor.psi.stub.impl;

import consulo.cpp.preprocessor.psi.CPreprocessorIfBlock;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBlockStub;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubBase;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.Nullable;

public class CPreprocessorIfBlockStubImpl extends StubBase<CPreprocessorIfBlock> implements CPreprocessorIfBlockStub {
    private final String myVariableName;
    private final boolean myReverted;

    public CPreprocessorIfBlockStubImpl(StubElement<?> parent,
                                        IStubElementType<?, ?> elementType,
                                        @Nullable String variableName,
                                        boolean reverted) {
        super(parent, elementType);
        myVariableName = variableName;
        myReverted = reverted;
    }

    @Override
    @Nullable
    public String getVariableName() {
        return myVariableName;
    }

    @Override
    public boolean isReverted() {
        return myReverted;
    }
}
