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

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.preprocessor.lexer._CPreprocessorLexer;
import consulo.cpp.preprocessor.parser.CPreprocessorParser;
import consulo.language.ast.TokenSet;
import consulo.language.ast.TokenType;
import consulo.language.lexer.FlexAdapter;
import consulo.language.lexer.Lexer;
import consulo.language.parser.PsiParser;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionWithParsing;
import jakarta.inject.Inject;

/**
 * The version of the preprocessor layer of a C/C++ file. Which one a file uses follows from its
 * {@link consulo.cpp.lang.version.CLanguageVersion#getPreprocessorVersion() C version}, so a dialect with directives
 * of its own - the {@code #import} of Objective-C - registers a subclass with its own lexer and parser.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
@ExtensionImpl
public class CPreprocessorLanguageVersion extends LanguageVersion implements LanguageVersionWithParsing {
    public static final String ID = "C_PREPROCESSOR";

    @Inject
    public CPreprocessorLanguageVersion() {
        this(ID, "C Preprocessor");
    }

    protected CPreprocessorLanguageVersion(String id, String name) {
        super(id, name, CPreprocessorLanguage.INSTANCE);
    }

    @Override
    public Lexer createLexer() {
        return new FlexAdapter(new _CPreprocessorLexer());
    }

    @Override
    public PsiParser createParser() {
        return new CPreprocessorParser();
    }

    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(TokenType.WHITE_SPACE);
    }

    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }
}
