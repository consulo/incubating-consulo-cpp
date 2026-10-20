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

import consulo.cpp.preprocessor.expand.CPreprocessorExpandContext;
import consulo.cpp.preprocessor.psi.CPreprocessorUndefDirective;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.Nullable;

/**
 * An {@code #undef} has to be in the stub tree as well: a header which undefines a macro must take it away from
 * the files that include it, and the include walk reads stubs rather than PSI.
 */
public interface CPreprocessorUndefStub extends StubElement<CPreprocessorUndefDirective>, CPreprocessorDirectiveStub {
    @Nullable
    String getName();

    @Override
    default void expand(CPreprocessorExpandContext context) {
        String name = getName();
        if (name != null) {
            context.undefine(name);
        }
    }
}
