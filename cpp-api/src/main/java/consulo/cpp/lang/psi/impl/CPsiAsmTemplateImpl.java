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

import consulo.cpp.lang.psi.CPsiAsmTemplate;
import consulo.cpp.lang.psi.CPsiTokens;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.LiteralTextEscaper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-20
 */
public class CPsiAsmTemplateImpl extends CPsiAsmCodeImpl implements CPsiAsmTemplate {
    public CPsiAsmTemplateImpl(ASTNode node) {
        super(node);
    }

    /**
     * The contents of the string literals written here - not those a macro among them expands to, which have no text
     * in this file.
     */
    @Override
    public List<TextRange> getCodeRanges() {
        List<TextRange> ranges = new ArrayList<>();
        int hostStart = getNode().getStartOffset();
        for (ASTNode child = getNode().getFirstChildNode(); child != null; child = child.getTreeNext()) {
            if (child.getElementType() != CPsiTokens.STRING_LITERAL || child.getTextLength() == 0) {
                continue;
            }
            String text = child.getText();
            // a prefix - L"...", u8"..." - comes before the opening quote
            int open = text.indexOf('"');
            if (open < 0 || text.length() - open < 2 || text.charAt(text.length() - 1) != '"') {
                continue;
            }
            int start = child.getStartOffset() - hostStart;
            ranges.add(new TextRange(start + open + 1, start + text.length() - 1));
        }
        return ranges;
    }

    @Override
    public boolean isBlock() {
        return false;
    }

    @Override
    public boolean isExtended() {
        for (ASTNode next = getNode().getTreeNext(); next != null; next = next.getTreeNext()) {
            if (next.getElementType() == CPsiTokens.COLON) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LiteralTextEscaper<CPsiAsmTemplateImpl> createLiteralTextEscaper() {
        return new CStringLiteralEscaper<>(this, isExtended());
    }
}
