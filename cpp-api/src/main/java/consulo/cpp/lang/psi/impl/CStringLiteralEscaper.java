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
package consulo.cpp.lang.psi.impl;

import consulo.document.util.TextRange;
import consulo.language.psi.LiteralTextEscaper;
import consulo.language.psi.PsiLanguageInjectionHost;

import java.util.Arrays;

/**
 * Decodes the contents of a C string literal - {@code \n}, {@code \t}, {@code \"}, octal and hexadecimal escapes -
 * keeping, for every decoded character, where it was written.
 *
 * @author VISTALL
 * @since 2026-10-20
 */
public class CStringLiteralEscaper<T extends PsiLanguageInjectionHost> extends LiteralTextEscaper<T> {
    private final boolean myPercentEscapes;
    private int[] mySourceOffsets = new int[0];

    /**
     * @param percentEscapes whether {@code %%} stands for one {@code %}, as in the template of an extended asm
     */
    public CStringLiteralEscaper(T host, boolean percentEscapes) {
        super(host);
        myPercentEscapes = percentEscapes;
    }

    @Override
    public boolean decode(TextRange rangeInsideHost, StringBuilder outChars) {
        String text = rangeInsideHost.substring(myHost.getText());
        int[] offsets = new int[text.length() + 1];
        int count = 0;
        int i = 0;
        while (i < text.length()) {
            offsets[count++] = i;
            char c = text.charAt(i);
            if (c == '\\' && i + 1 < text.length()) {
                i = decodeEscape(text, i + 1, outChars);
            }
            else if (myPercentEscapes && c == '%' && i + 1 < text.length() && text.charAt(i + 1) == '%') {
                outChars.append('%');
                i += 2;
            }
            else {
                outChars.append(c);
                i++;
            }
        }
        offsets[count] = text.length();
        mySourceOffsets = Arrays.copyOf(offsets, count + 1);
        return true;
    }

    /**
     * @return the index after the escape
     */
    private static int decodeEscape(String text, int at, StringBuilder out) {
        char c = text.charAt(at);
        switch (c) {
            case 'n' -> out.append('\n');
            case 't' -> out.append('\t');
            case 'r' -> out.append('\r');
            case 'a' -> out.append('\u0007');
            case 'b' -> out.append('\b');
            case 'f' -> out.append('\f');
            case 'v' -> out.append('\u000B');
            case 'x' -> {
                int end = at + 1;
                while (end < text.length() && Character.digit(text.charAt(end), 16) >= 0) {
                    end++;
                }
                if (end == at + 1) {
                    out.append('x');
                    return at + 1;
                }
                out.append((char) Integer.parseInt(text.substring(at + 1, end), 16));
                return end;
            }
            default -> {
                if (c >= '0' && c <= '7') {
                    int end = at;
                    while (end < text.length() && end < at + 3 && text.charAt(end) >= '0' && text.charAt(end) <= '7') {
                        end++;
                    }
                    out.append((char) Integer.parseInt(text.substring(at, end), 8));
                    return end;
                }
                // \\, \", \' and anything unknown stand for the character itself
                out.append(c);
            }
        }
        return at + 1;
    }

    @Override
    public int getOffsetInHost(int offsetInDecoded, TextRange rangeInsideHost) {
        if (offsetInDecoded < 0 || offsetInDecoded >= mySourceOffsets.length) {
            return -1;
        }
        return rangeInsideHost.getStartOffset() + mySourceOffsets[offsetInDecoded];
    }

    @Override
    public boolean isOneLine() {
        return false;
    }
}
