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
package consulo.cpp.lang.parser;

import consulo.cpp.lang.psi.CElementTypes;
import consulo.cpp.localize.CLocalize;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;

/**
 * Statements and code blocks.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
public class CStatementParsing extends CParsingBase {
    public CStatementParsing(CParsingContext context) {
        super(context);
    }

    /**
     * {@code { statement* }} - at the {@code {}.
     */
    public void parseCodeBlock() {
        PsiBuilder.Marker marker = mark();
        advance();

        while (!eof() && !at(RBRACE)) {
            parseStatement();
        }

        expect(RBRACE, CLocalize.rbraceExpected());
        marker.done(CElementTypes.CODE_BLOCK);
    }

    /**
     * Parses one statement; always consumes at least one token.
     */
    public void parseStatement() {
        IElementType type = token();

        if (type == LBRACE) {
            parseCodeBlock();
        }
        else if (type == IF_KEYWORD) {
            parseIfStatement();
        }
        else if (type == WHILE_KEYWORD) {
            parseWhileStatement();
        }
        else if (type == DO_KEYWORD) {
            parseDoWhileStatement();
        }
        else if (type == FOR_KEYWORD) {
            parseForStatement();
        }
        else if (type == SWITCH_KEYWORD) {
            parseSwitchStatement();
        }
        else if (type == CASE_KEYWORD) {
            parseCaseStatement();
        }
        else if (type == DEFAULT_KEYWORD) {
            parseDefaultStatement();
        }
        else if (type == RETURN_KEYWORD) {
            parseReturnStatement();
        }
        else if (type == BREAK_KEYWORD) {
            parseJumpStatement(CElementTypes.BREAK_STATEMENT);
        }
        else if (type == CONTINUE_KEYWORD) {
            parseJumpStatement(CElementTypes.CONTINUE_STATEMENT);
        }
        else if (type == GOTO_KEYWORD) {
            parseGotoStatement();
        }
        else if (type == TRY_KEYWORD) {
            parseTryStatement();
        }
        else if (type == SEMICOLON) {
            // the null statement
            advance();
        }
        else if (type == IDENTIFIER && lookAhead(1) == COLON) {
            parseLabeledStatement();
        }
        else if (type == STATIC_ASSERT_KEYWORD) {
            declarations().parseStaticAssert();
        }
        else if (type == ASM_KEYWORD) {
            declarations().parseAsm();
        }
        else if (declarations().looksLikeDeclaration()) {
            declarations().parseDeclaration(CDeclarationParsing.Scope.BLOCK);
        }
        else {
            parseExpressionStatement();
        }
    }

    private void parseExpressionStatement() {
        PsiBuilder.Marker marker = mark();
        if (!expressions().parseExpression()) {
            marker.drop();

            PsiBuilder.Marker error = mark();
            advance();
            error.error(CLocalize.statementExpected());
            return;
        }

        expect(SEMICOLON, CLocalize.semicolonExpected());
        marker.done(CElementTypes.EXPRESSION_STATEMENT);
    }

    private void parseCondition() {
        if (expect(LPARENTH, CLocalize.lparenthExpected())) {
            expressions().parseExpressionOrError();
            expect(RPARENTH, CLocalize.rparenthExpected());
        }
    }

    private void parseBody() {
        if (eof() || at(RBRACE)) {
            error(CLocalize.statementExpected());
        }
        else {
            parseStatement();
        }
    }

    private void parseIfStatement() {
        PsiBuilder.Marker marker = mark();
        advance();
        // if constexpr (C++17)
        consume(CONSTEXPR_KEYWORD);

        parseCondition();
        parseBody();

        if (consume(ELSE_KEYWORD)) {
            parseBody();
        }
        marker.done(CElementTypes.IF_STATEMENT);
    }

    private void parseWhileStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        parseCondition();
        parseBody();
        marker.done(CElementTypes.WHILE_STATEMENT);
    }

    private void parseDoWhileStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        parseBody();

        if (expect(WHILE_KEYWORD, CLocalize.whileExpected())) {
            parseCondition();
        }
        expect(SEMICOLON, CLocalize.semicolonExpected());
        marker.done(CElementTypes.DO_WHILE_STATEMENT);
    }

    /**
     * {@code for (init; condition; update) body}, the init a declaration or an expression; and the C++ range form,
     * {@code for (declaration : range) body}.
     */
    private void parseForStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (expect(LPARENTH, CLocalize.lparenthExpected())) {
            boolean range = false;
            if (consume(SEMICOLON)) {
                // no init
            }
            else if (declarations().looksLikeDeclaration()) {
                declarations().parseDeclaration(CDeclarationParsing.Scope.FOR_INIT);
                range = consume(COLON);
            }
            else {
                expressions().parseExpressionOrError();
                expect(SEMICOLON, CLocalize.semicolonExpected());
            }

            if (range) {
                if (!expressions().parseInitializer()) {
                    error(CLocalize.expressionExpected());
                }
            }
            else {
                if (!at(SEMICOLON)) {
                    expressions().parseExpressionOrError();
                }
                expect(SEMICOLON, CLocalize.semicolonExpected());

                if (!at(RPARENTH)) {
                    expressions().parseExpressionOrError();
                }
            }
            expect(RPARENTH, CLocalize.rparenthExpected());
        }

        parseBody();
        marker.done(CElementTypes.FOR_STATEMENT);
    }

    private void parseSwitchStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        parseCondition();
        parseBody();
        marker.done(CElementTypes.SWITCH_STATEMENT);
    }

    /**
     * {@code case value:} - the statements after it are its siblings. GNU ranges, {@code case 1 ... 5:}, included.
     */
    private void parseCaseStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        expressions().parseConditionalExpressionOrError();
        if (consume(ELLIPSIS)) {
            expressions().parseConditionalExpressionOrError();
        }
        expect(COLON, CLocalize.colonExpected());
        marker.done(CElementTypes.CASE_STATEMENT);
    }

    private void parseDefaultStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        expect(COLON, CLocalize.colonExpected());
        marker.done(CElementTypes.DEFAULT_STATEMENT);
    }

    private void parseReturnStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (!at(SEMICOLON)) {
            if (!expressions().parseInitializer()) {
                error(CLocalize.expressionExpected());
            }
            else {
                // return a, b;
                while (consume(COMMA)) {
                    expressions().parseAssignmentExpressionOrError();
                }
            }
        }
        expect(SEMICOLON, CLocalize.semicolonExpected());
        marker.done(CElementTypes.RETURN_STATEMENT);
    }

    private void parseJumpStatement(IElementType elementType) {
        PsiBuilder.Marker marker = mark();
        advance();

        expect(SEMICOLON, CLocalize.semicolonExpected());
        marker.done(elementType);
    }

    private void parseGotoStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (consume(ASTERISK)) {
            // GNU computed goto, goto *target;
            expressions().parseExpressionOrError();
        }
        else {
            expect(IDENTIFIER, CLocalize.identifierExpected());
        }
        expect(SEMICOLON, CLocalize.semicolonExpected());
        marker.done(CElementTypes.GOTO_STATEMENT);
    }

    private void parseLabeledStatement() {
        PsiBuilder.Marker marker = mark();
        advance();
        advance();

        // a label may close a block: `end: }`
        if (!eof() && !at(RBRACE)) {
            parseStatement();
        }
        marker.done(CElementTypes.LABELED_STATEMENT);
    }

    /**
     * {@code try { } catch (declaration) { }...}
     */
    private void parseTryStatement() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (at(LBRACE)) {
            parseCodeBlock();
        }
        else {
            error(CLocalize.lbraceExpected());
        }

        while (at(CATCH_KEYWORD)) {
            advance();
            if (at(LPARENTH)) {
                declarations().parseParameterList();
            }
            if (at(LBRACE)) {
                parseCodeBlock();
            }
            else {
                error(CLocalize.lbraceExpected());
                break;
            }
        }
        marker.done(CElementTypes.CODE_BLOCK);
    }
}
