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

package consulo.cpp.parsing;

import consulo.cpp.CParsingBaseTest;
import consulo.cpp.lang.CSourceFileType;
import consulo.language.file.LanguageFileType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Tests for preprocessor macro expansion in C source files.
 *
 * <p>Each test verifies that macros defined with {@code #define} are correctly expanded
 * when referenced in C code. Fixture files are stored under
 * {@code src/test/resources/parsing/expand/} with per-language expected trees:
 * <ul>
 * <li>{@code TestName.c}             – C source input</li>
 * <li>{@code TestName.C.txt}         – expected C primary root (after expansion)</li>
 * <li>{@code TestName.CPreprocessor.txt} – expected preprocessor root</li>
 * </ul>
 */
public class CExpandTest extends CParsingBaseTest {
    public CExpandTest() {
        super("parsing/expand", "c");
    }

    @Nonnull
    @Override
    protected LanguageFileType getFileType(@Nonnull Context context, @Nullable Object testContext) {
        return CSourceFileType.INSTANCE;
    }

    @Override
    protected boolean checkAllPsiRoots() {
        return true;
    }

    /**
     * {@code #define MY_INT int} used in a global variable declaration
     */
    @Test
    public void testTypedefExpand(Context context) throws Exception {
        doTest(context, null);
    }

    /**
     * {@code #define VOID void} used in a function declaration
     */
    @Test
    public void testFunctionReturnTypeExpand(Context context) throws Exception {
        doTest(context, null);
    }

    /**
     * {@code #define FOO 1\n#undef FOO\n#define FOO 2} – undef then redefine
     */
    @Test
    public void testUndefAndRedefine(Context context) throws Exception {
        doTest(context, null);
    }
}
