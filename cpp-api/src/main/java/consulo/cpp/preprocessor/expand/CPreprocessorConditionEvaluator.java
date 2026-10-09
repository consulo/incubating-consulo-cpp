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

import java.util.Map;

/**
 * Evaluates the controlling expression of {@code #if} and {@code #elif}.
 * <p>
 * Supports what conditional compilation actually relies on: {@code defined}, the boolean and comparison
 * operators, parentheses, integer literals, and macro names - an undefined name is {@code 0}, as in C. Anything
 * it cannot make sense of evaluates to false rather than throwing, so a malformed condition simply hides its
 * branch instead of breaking the parse.
 */
public final class CPreprocessorConditionEvaluator {
    private final String myText;
    private final Map<String, String> myDefines;
    private int myPosition;

    private CPreprocessorConditionEvaluator(String text, Map<String, String> defines) {
        myText = text;
        myDefines = defines;
    }

    public static boolean evaluate(String condition, Map<String, String> defines) {
        if (condition == null) {
            return false;
        }
        String trimmed = condition.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        try {
            CPreprocessorConditionEvaluator evaluator = new CPreprocessorConditionEvaluator(trimmed, defines);
            long value = evaluator.parseOr(0);
            evaluator.skipSpaces();
            return evaluator.myPosition >= trimmed.length() && value != 0;
        }
        catch (RuntimeException malformed) {
            return false;
        }
    }

    private long parseOr(int depth) {
        long value = parseAnd(depth);
        while (eat("||")) {
            long right = parseAnd(depth);
            value = (value != 0 || right != 0) ? 1 : 0;
        }
        return value;
    }

    private long parseAnd(int depth) {
        long value = parseEquality(depth);
        while (eat("&&")) {
            long right = parseEquality(depth);
            value = (value != 0 && right != 0) ? 1 : 0;
        }
        return value;
    }

    private long parseEquality(int depth) {
        long value = parseRelational(depth);
        while (true) {
            if (eat("==")) {
                value = value == parseRelational(depth) ? 1 : 0;
            }
            else if (eat("!=")) {
                value = value != parseRelational(depth) ? 1 : 0;
            }
            else {
                return value;
            }
        }
    }

    private long parseRelational(int depth) {
        long value = parseUnary(depth);
        while (true) {
            if (eat("<=")) {
                value = value <= parseUnary(depth) ? 1 : 0;
            }
            else if (eat(">=")) {
                value = value >= parseUnary(depth) ? 1 : 0;
            }
            else if (eat("<")) {
                value = value < parseUnary(depth) ? 1 : 0;
            }
            else if (eat(">")) {
                value = value > parseUnary(depth) ? 1 : 0;
            }
            else {
                return value;
            }
        }
    }

    private long parseUnary(int depth) {
        skipSpaces();
        if (eat("!")) {
            return parseUnary(depth) == 0 ? 1 : 0;
        }
        if (eat("(")) {
            long value = parseOr(depth + 1);
            eat(")");
            return value;
        }

        String word = readWord();
        if (word.isEmpty()) {
            throw new IllegalStateException("not a condition");
        }
        if ("defined".equals(word)) {
            boolean parenthesised = eat("(");
            String name = readWord();
            if (parenthesised) {
                eat(")");
            }
            return myDefines.containsKey(name) ? 1 : 0;
        }
        return valueOf(word, depth);
    }

    /**
     * A macro used in a condition contributes its replacement text, which may itself be a macro; the depth guard
     * keeps a self-referential define from recursing forever.
     */
    private long valueOf(String word, int depth) {
        if (isNumber(word)) {
            return parseNumber(word);
        }
        String replacement = myDefines.get(word);
        if (replacement == null) {
            return 0;
        }
        String trimmed = replacement.trim();
        if (trimmed.isEmpty()) {
            return 1;
        }
        if (depth > 16) {
            return 0;
        }
        CPreprocessorConditionEvaluator nested = new CPreprocessorConditionEvaluator(trimmed, myDefines);
        long value = nested.parseOr(depth + 1);
        return value;
    }

    private static boolean isNumber(String word) {
        return !word.isEmpty() && Character.isDigit(word.charAt(0));
    }

    private static long parseNumber(String word) {
        String digits = word;
        while (!digits.isEmpty() && "uUlL".indexOf(digits.charAt(digits.length() - 1)) >= 0) {
            digits = digits.substring(0, digits.length() - 1);
        }
        try {
            if (digits.startsWith("0x") || digits.startsWith("0X")) {
                return Long.parseLong(digits.substring(2), 16);
            }
            return Long.parseLong(digits);
        }
        catch (NumberFormatException notANumber) {
            return 0;
        }
    }

    private String readWord() {
        skipSpaces();
        int start = myPosition;
        while (myPosition < myText.length()
            && (Character.isLetterOrDigit(myText.charAt(myPosition)) || myText.charAt(myPosition) == '_')) {
            myPosition++;
        }
        return myText.substring(start, myPosition);
    }

    private boolean eat(String token) {
        skipSpaces();
        if (myText.startsWith(token, myPosition)) {
            myPosition += token.length();
            return true;
        }
        return false;
    }

    private void skipSpaces() {
        while (myPosition < myText.length() && Character.isWhitespace(myText.charAt(myPosition))) {
            myPosition++;
        }
    }
}
