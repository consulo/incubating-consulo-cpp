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

package consulo.cpp.preprocessor.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.cpp.preprocessor.expand.CPreprocessorExpandContext;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.psi.PsiNamedElement;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @date 7:23/11.12.2011
 */
public interface CPreprocessorDefineDirective extends CPreprocessorDirective, PsiNameIdentifierOwner, PsiNamedElement {
    CPsiSharpDefineValue getValue();

    /**
     * The parameters of a function-like macro - {@code __VA_ARGS__}'s {@code ...} included - or {@code null} for an
     * object-like one.
     */
    @Nullable
    List<String> getParameters();

    @Override
    @RequiredReadAction
    default void expand(CPreprocessorExpandContext context) {
        context.removeFromSource(getTextRange());

        String name = getName();
        if (name != null) {
            CPsiSharpDefineValue value = getValue();
            context.define(name, getParameters(), value == null ? "" : value.getNode().getChars(), this);
        }
    }
}
