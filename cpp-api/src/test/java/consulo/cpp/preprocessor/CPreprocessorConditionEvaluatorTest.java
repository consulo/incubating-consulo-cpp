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
package consulo.cpp.preprocessor;

import consulo.cpp.preprocessor.expand.CPreprocessorConditionEvaluator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CPreprocessorConditionEvaluatorTest {
    private static final Map<String, String> DEFINES = Map.of(
        "DEBUG", "1",
        "ZERO", "0",
        "GUARD", "",
        "LEVEL", "3",
        "ALIAS", "LEVEL"
    );

    private static boolean eval(String condition) {
        return CPreprocessorConditionEvaluator.evaluate(condition, DEFINES);
    }

    @Test
    public void literals() {
        assertTrue(eval("1"));
        assertFalse(eval("0"));
        assertTrue(eval("0x10"));
        assertTrue(eval("2u"));
    }

    @Test
    public void definedOperator() {
        assertTrue(eval("defined(DEBUG)"));
        assertTrue(eval("defined DEBUG"));
        assertFalse(eval("defined(MISSING)"));
        assertTrue(eval("!defined(MISSING)"));
    }

    /** A name with no replacement text is still defined, so it is true in a condition. */
    @Test
    public void valuelessDefineIsTrue() {
        assertTrue(eval("defined(GUARD)"));
        assertTrue(eval("GUARD"));
    }

    @Test
    public void undefinedNameIsZero() {
        assertFalse(eval("MISSING"));
        assertTrue(eval("MISSING == 0"));
    }

    @Test
    public void booleanOperators() {
        assertTrue(eval("defined(DEBUG) && !defined(MISSING)"));
        assertFalse(eval("defined(DEBUG) && defined(MISSING)"));
        assertTrue(eval("defined(MISSING) || DEBUG"));
        assertFalse(eval("ZERO || MISSING"));
    }

    @Test
    public void comparisons() {
        assertTrue(eval("LEVEL >= 3"));
        assertFalse(eval("LEVEL > 3"));
        assertTrue(eval("LEVEL != 0"));
        assertTrue(eval("LEVEL == 3"));
    }

    @Test
    public void parentheses() {
        assertTrue(eval("(defined(MISSING) || DEBUG) && LEVEL >= 3"));
        assertFalse(eval("defined(MISSING) || (DEBUG && ZERO)"));
    }

    /** A macro whose replacement is another macro resolves through. */
    @Test
    public void macroValuesResolve() {
        assertTrue(eval("ALIAS == 3"));
    }

    /** A condition that cannot be parsed hides its branch rather than breaking the parse. */
    @Test
    public void malformedIsFalse() {
        assertFalse(eval("&&"));
        assertFalse(eval(""));
        assertFalse(eval(null));
        assertFalse(eval("1 +"));
    }
}
