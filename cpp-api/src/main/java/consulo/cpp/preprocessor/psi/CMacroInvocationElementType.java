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

import consulo.cpp.lang.CLanguage;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorMacroInvocationImpl;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.ILeafElementType;

/**
 * The token of a macro use in the C text.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
public final class CMacroInvocationElementType extends IElementType implements ILeafElementType {
    CMacroInvocationElementType() {
        super("MACRO_INVOCATION", CLanguage.INSTANCE);
    }

    @Override
    public ASTNode createLeafNode(CharSequence leafText) {
        return new CPreprocessorMacroInvocationImpl(this, leafText);
    }
}
