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
import consulo.cpp.lang.CPPSourceFileType;
import consulo.language.file.LanguageFileType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Parsing tests for the C++ language (.cpp files).
 *
 * <p>Each test method corresponds to a pair of resource files under
 * {@code src/test/resources/parsing/cpp/}:
 * <ul>
 * <li>{@code MethodName.cpp} – C++ source input</li>
 * <li>{@code MethodName.txt} – expected PSI tree dump</li>
 * </ul>
 */
public class CPPParsingTest extends CParsingBaseTest {
    public CPPParsingTest() {
        super("parsing/cpp", "cpp");
    }

    @Nonnull
    @Override
    protected LanguageFileType getFileType(@Nonnull Context context, @Nullable Object testContext) {
        return CPPSourceFileType.INSTANCE;
    }

    /**
     * {@code class Foo { public: void bar(); };} – class with a method declaration
     */
    @Test
    public void testClass(Context context) throws Exception {
        doTest(context, null);
    }

    /**
     * {@code namespace std { void foo(); }} – namespace with a method declaration
     */
    @Test
    public void testNamespace(Context context) throws Exception {
        doTest(context, null);
    }

    /**
     * {@code class Base {}; class Child : public Base {};} – class inheritance
     */
    @Test
    public void testInheritance(Context context) throws Exception {
        doTest(context, null);
    }
}
