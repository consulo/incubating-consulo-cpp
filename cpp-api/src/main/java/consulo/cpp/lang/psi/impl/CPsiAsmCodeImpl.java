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

import consulo.cpp.lang.CLanguage;
import consulo.cpp.lang.psi.CPsiAsmCode;
import consulo.document.Document;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.language.psi.util.PsiTreeUtil;

/**
 * @author VISTALL
 * @since 2026-10-20
 */
public abstract class CPsiAsmCodeImpl extends CPsiElementBaseImpl implements CPsiAsmCode {
    protected CPsiAsmCodeImpl(ASTNode node) {
        super(node);
    }

    @Override
    public boolean isValidHost() {
        return !getCodeRanges().isEmpty();
    }

    /**
     * The C tree is built from the text with the directives cut out and the macros expanded, so it is not edited in
     * place: the text goes to the document, and the host is the one the reparse puts at the same offset.
     */
    @Override
    public PsiLanguageInjectionHost updateText(String text) {
        PsiFile file = getContainingFile();
        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(getProject());
        Document document = documentManager.getDocument(file);
        if (document == null) {
            return this;
        }
        TextRange range = getTextRange();
        document.replaceString(range.getStartOffset(), range.getEndOffset(), text);
        documentManager.commitDocument(document);

        PsiElement at = file.getViewProvider().findElementAt(range.getStartOffset(), CLanguage.INSTANCE);
        CPsiAsmCode host = PsiTreeUtil.getParentOfType(at, CPsiAsmCode.class, false);
        return host != null ? host : this;
    }
}
