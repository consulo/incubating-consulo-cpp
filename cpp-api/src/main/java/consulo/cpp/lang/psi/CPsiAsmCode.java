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
package consulo.cpp.lang.psi;

import consulo.document.util.TextRange;
import consulo.language.psi.PsiLanguageInjectionHost;

import java.util.List;

/**
 * Assembly written in a C/C++ file: the template of {@code asm("...")}, or the body of an MSVC {@code __asm { ... }}
 * block. Another plugin parses it as assembly, through injection.
 *
 * @author VISTALL
 * @since 2026-10-20
 */
public interface CPsiAsmCode extends CPsiElement, PsiLanguageInjectionHost {
    /**
     * @return the ranges, relative to this element, which hold the assembly - the contents of each string literal of a
     * template, without the quotes; the inside of a block, without the braces
     */
    List<TextRange> getCodeRanges();

    /**
     * @return whether the assembly is the Intel syntax of an MSVC block rather than a GNU as template
     */
    boolean isBlock();
}
