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

import consulo.cpp.lang.psi.CPsiAsmBlock;
import consulo.cpp.lang.psi.CPsiTokens;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.LiteralTextEscaper;

import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-20
 */
public class CPsiAsmBlockImpl extends CPsiAsmCodeImpl implements CPsiAsmBlock {
    public CPsiAsmBlockImpl(ASTNode node) {
        super(node);
    }

    @Override
    public List<TextRange> getCodeRanges() {
        ASTNode open = getNode().getFirstChildNode();
        ASTNode close = getNode().getLastChildNode();
        if (open == null || open.getElementType() != CPsiTokens.LBRACE) {
            return List.of();
        }
        int end = close != null && close != open && close.getElementType() == CPsiTokens.RBRACE ? close.getStartOffsetInParent() : getTextLength();
        int start = open.getTextLength();
        return end > start ? List.of(new TextRange(start, end)) : List.of();
    }

    @Override
    public boolean isBlock() {
        return true;
    }

    /**
     * The body is written as it is, nothing to decode.
     */
    @Override
    public LiteralTextEscaper<CPsiAsmBlockImpl> createLiteralTextEscaper() {
        return new LiteralTextEscaper<>(this) {
            @Override
            public boolean decode(TextRange rangeInsideHost, StringBuilder outChars) {
                outChars.append(rangeInsideHost.substring(myHost.getText()));
                return true;
            }

            @Override
            public int getOffsetInHost(int offsetInDecoded, TextRange rangeInsideHost) {
                int offset = rangeInsideHost.getStartOffset() + offsetInDecoded;
                return offset <= rangeInsideHost.getEndOffset() ? offset : -1;
            }

            @Override
            public boolean isOneLine() {
                return false;
            }
        };
    }
}
