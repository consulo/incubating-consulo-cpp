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
package consulo.cpp.asm;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.psi.CPsiAsmCode;
import consulo.document.util.TextRange;
import consulo.language.inject.MultiHostInjector;
import consulo.language.inject.MultiHostRegistrar;
import consulo.language.psi.PsiElement;

import java.util.List;

/**
 * Parses inline assembly as assembly: the template of {@code asm("...")} - all its string literals as one piece, so an
 * instruction may continue in the next literal - and the body of an MSVC {@code __asm { ... }} block.
 *
 * @author VISTALL
 * @since 2026-10-20
 */
@ExtensionImpl
public class CAsmInjector implements MultiHostInjector {
    @Override
    public Class<? extends PsiElement> getElementClass() {
        return CPsiAsmCode.class;
    }

    @Override
    public void injectLanguages(MultiHostRegistrar registrar, PsiElement context) {
        if (!(context instanceof CPsiAsmCode code) || !code.isValidHost()) {
            return;
        }
        List<TextRange> ranges = code.getCodeRanges();
        registrar.startInjecting(CAsmDialects.dialectOf(code));
        for (TextRange range : ranges) {
            registrar.addPlace(null, null, code, range);
        }
        registrar.doneInjecting();
    }
}
