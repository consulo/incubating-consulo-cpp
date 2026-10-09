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
package consulo.cpp.preprocessor.psi.stub;

import consulo.cpp.preprocessor.psi.CPreprocessorIfBlock;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.Nullable;

/**
 * Conditionals must survive into the stub tree: a file which includes a header reads that header through stubs,
 * and without the condition it would take the defines of every branch instead of the active one.
 */
public interface CPreprocessorIfBlockStub extends StubElement<CPreprocessorIfBlock> {
    @Nullable
    String getVariableName();

    boolean isReverted();
}
