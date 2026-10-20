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
package consulo.cpp.preprocessor.expand;

import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.document.util.TextRange;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * What a {@link consulo.cpp.preprocessor.psi.CPreprocessorDirective directive} can do to the expansion of a file:
 * take its own text out of what the C parser sees, and change the macros in scope - directly, or by pulling in
 * another file. Directives are applied in source order, and only those of the conditional branches that are compiled.
 * <p>
 * The same context serves a directive of the file being parsed and one of a header it includes, which is applied from
 * the header's stubs; there {@link #removeFromSource} has nothing to remove and does nothing.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
public interface CPreprocessorExpandContext {
    /**
     * Removes {@code range} of the file from the text the C parser sees; the directive stays in the tree as an outer
     * element.
     */
    void removeFromSource(TextRange range);

    /**
     * Defines {@code name} with {@code value} as its replacement text, replacing any earlier definition.
     *
     * @param parameters the parameters of a function-like macro, {@code null} for an object-like one
     * @param element    the directive to navigate to from a use of the macro, {@code null} when there is none
     */
    void define(String name, @Nullable List<String> parameters, CharSequence value, @Nullable CPreprocessorDefineDirective element);

    void undefine(String name);

    boolean isDefined(String name);

    /**
     * Brings in the macros of the file {@code path} names. A quoted include ({@code system == false}) is looked up next
     * to the including file first, then in the include paths; a system include only in the include paths. A file is
     * read once per expansion, however often it is included.
     */
    void include(String path, boolean system);
}
