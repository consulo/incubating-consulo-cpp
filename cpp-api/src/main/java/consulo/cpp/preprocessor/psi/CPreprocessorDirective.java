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

import consulo.annotation.access.RequiredReadAction;
import consulo.cpp.preprocessor.expand.CPreprocessorExpandContext;

/**
 * A directive which acts on the expansion of its file - {@code #define}, {@code #include}, and whatever directives a
 * dialect adds, such as the {@code #import} of Objective-C. The expander applies every directive of a compiled branch
 * in source order; conditional blocks are not directives, the expander picks their branch itself.
 * <p>
 * A directive which also matters when its file is included by another - one that defines a macro or includes a
 * file - keeps a stub implementing {@link consulo.cpp.preprocessor.psi.stub.CPreprocessorDirectiveStub}.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
public interface CPreprocessorDirective extends CPreprocessorElement {
    @RequiredReadAction
    void expand(CPreprocessorExpandContext context);
}
