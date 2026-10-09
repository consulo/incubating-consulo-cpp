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

import consulo.cpp.preprocessor.psi.CPsiSharpIfBody;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBodyStub;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubBase;
import consulo.language.psi.stub.StubElement;

public class CPreprocessorIfBodyStubImpl extends StubBase<CPsiSharpIfBody> implements CPreprocessorIfBodyStub {
    private final boolean myElseBody;

    public CPreprocessorIfBodyStubImpl(StubElement<?> parent, IStubElementType<?, ?> elementType, boolean elseBody) {
        super(parent, elementType);
        myElseBody = elseBody;
    }

    @Override
    public boolean isElseBody() {
        return myElseBody;
    }
}
