/*
 * Copyright 2011 napile
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

package org.napile.cpp4idea.lang.parser.parsingMain;

import consulo.cpp.localize.CLocalize;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import org.napile.cpp4idea.lang.psi.CElementTypes;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 10:36/13.12.2011
 */
public class CodeBlockParsing extends MainParsing {
    public static void parseCodeBlock(PsiBuilder builder) {
        PsiBuilder.Marker marker;

        if (builder.getTokenType() != LBRACE) {
            builder.error(CLocalize.lbraceExpected().get());
            marker = builder.mark();
            builder.advanceLexer();

            marker.done(CElementTypes.PARAMETER_LIST);
            return;
        }
        else {
            marker = builder.mark();
            builder.advanceLexer();
        }

        if (builder.getTokenType() != RBRACE) {
            while (!builder.eof()) {
                parseStatement(builder);

                if (builder.getTokenType() == RBRACE) {
                    break;
                }
            }

            if (builder.getTokenType() != RBRACE) {
                builder.error(CLocalize.rbraceExpected().get());
            }
        }

        builder.advanceLexer();

        marker.done(CElementTypes.CODE_BLOCK);
    }

    static void parseStatement(PsiBuilder builder) {
        if (builder.getTokenType() == RETURN_KEYWORD) {
            parseReturnStatement(builder);
        }
        else if (builder.getTokenType() == IF_KEYWORD) {
            parseIfStatement(builder);
        }
        else if (builder.getTokenType() == WHILE_KEYWORD) {
            parseWhileStatement(builder);
        }
        else if (builder.getTokenType() == DO_KEYWORD) {
            parseDoWhileStatement(builder);
        }
        else if (builder.getTokenType() == FOR_KEYWORD) {
            parseForStatement(builder);
        }
        else if (builder.getTokenType() == SWITCH_KEYWORD) {
            parseSwitchStatement(builder);
        }
        else if (builder.getTokenType() == CASE_KEYWORD) {
            parseCaseStatement(builder);
        }
        else if (builder.getTokenType() == DEFAULT_KEYWORD) {
            parseDefaultStatement(builder);
        }
        else if (builder.getTokenType() == GOTO_KEYWORD) {
            parseGotoStatement(builder);
        }
        else if (builder.getTokenType() == BREAK_KEYWORD) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            consumeIf(builder, SEMICOLON);
            marker.done(CElementTypes.BREAK_STATEMENT);
        }
        else if (builder.getTokenType() == CONTINUE_KEYWORD) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            consumeIf(builder, SEMICOLON);
            marker.done(CElementTypes.CONTINUE_STATEMENT);
        }
        else if (builder.getTokenType() == SEMICOLON) {
            // null statement — a lone ';' is valid C; just consume it silently
            builder.advanceLexer();
        }
        else if (builder.getTokenType() == LBRACE) {
            parseCodeBlock(builder);
        }
        else {
            if (builder.getTokenType() == IDENTIFIER && builder.lookAhead(1) == COLON) {
                parseLabeledStatement(builder);
            }
            else if (looksLikeLocalVariable(builder)) {
                parseLocalVariable(builder);
            }
            else {
                parseExpressionStatement(builder);
            }
        }
    }

    /**
     * Returns {@code true} when the upcoming tokens look like a local variable
     * declaration rather than an expression statement.
     *
     * <ul>
     *   <li>A built-in type keyword ({@code int}, {@code char}, …) or a
     *       {@code signed}/{@code unsigned} prefix always starts a declaration.</li>
     *   <li>Two consecutive identifiers (e.g. {@code myType varName}) also indicate
     *       a declaration with a typedef'd type.</li>
     * </ul>
     */
    private static boolean looksLikeLocalVariable(PsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (CPsiTokens.TYPES.contains(token)
                || token == UNSIGNED_KEYWORD
                || token == SIGNED_KEYWORD) {
            return true;
        }
        if (token == IDENTIFIER) {
            // IDENTIFIER followed immediately by another IDENTIFIER → type + name
            return builder.lookAhead(1) == IDENTIFIER;
        }
        return false;
    }

    /**
     * Parses a local variable declaration:
     * {@code TypeRef name [dimensions] [= initializer] ;}
     */
    private static void parseLocalVariable(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        parseTypeRef(builder);

        if (builder.getTokenType() == IDENTIFIER) {
            builder.advanceLexer(); // variable name
        }

        // Optional array dimensions: buf[256] or buf[]
        while (consumeIf(builder, LBRACKET)) {
            if (builder.getTokenType() != RBRACKET) {
                ExpressionParsing.parseExpression(builder);
            }
            consumeIf(builder, RBRACKET);
        }

        // Optional initializer: = expr
        if (consumeIf(builder, EQ)) {
            ExpressionParsing.parseExpression(builder);
        }

        consumeIf(builder, SEMICOLON);

        marker.done(CElementTypes.LOCAL_VARIABLE);
    }

    /**
     * Parses an expression used as a statement:
     * {@code expr ;}
     *
     * <p>If no expression can be parsed at the current position the offending
     * token is skipped with an error, guaranteeing that the parser always makes
     * forward progress and never loops infinitely.
     */
    private static void parseExpressionStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        if (!ExpressionParsing.parseExpressionOptional(builder)) {
            builder.error("Unexpected token");
            builder.advanceLexer();
        }
        else {
            consumeIf(builder, SEMICOLON);
        }
        marker.done(CElementTypes.EXPRESSION_STATEMENT);
    }

    private static void parseReturnStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'return'

        // Only parse an expression when not immediately followed by ';' (void return)
        if (builder.getTokenType() != SEMICOLON) {
            ExpressionParsing.parseExpression(builder);
        }

        if (builder.getTokenType() != SEMICOLON) {
            builder.error(CLocalize.semicolonExpected().get());
        }
        else {
            builder.advanceLexer();
        }

        marker.done(CElementTypes.RETURN_STATEMENT);
    }

    private static void parseIfStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'if'

        if (builder.getTokenType() == LPARENTH) {
            builder.advanceLexer(); // consume '('
            ExpressionParsing.parseExpression(builder);
            consumeIf(builder, RPARENTH);
        }
        else {
            builder.error(CLocalize.lparenthExpected().get());
        }

        parseStatement(builder); // then-branch

        if (builder.getTokenType() == ELSE_KEYWORD) {
            builder.advanceLexer(); // consume 'else'
            parseStatement(builder); // else-branch
        }

        marker.done(CElementTypes.IF_STATEMENT);
    }

    private static void parseWhileStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'while'

        if (builder.getTokenType() == LPARENTH) {
            builder.advanceLexer(); // consume '('
            ExpressionParsing.parseExpression(builder);
            consumeIf(builder, RPARENTH);
        }
        else {
            builder.error(CLocalize.lparenthExpected().get());
        }

        parseStatement(builder); // body

        marker.done(CElementTypes.WHILE_STATEMENT);
    }

    /**
     * Parses a {@code do { body } while (condition);} loop.
     */
    private static void parseDoWhileStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'do'

        parseStatement(builder); // body

        if (builder.getTokenType() == WHILE_KEYWORD) {
            builder.advanceLexer(); // consume 'while'

            if (builder.getTokenType() == LPARENTH) {
                builder.advanceLexer(); // consume '('
                ExpressionParsing.parseExpression(builder);
                consumeIf(builder, RPARENTH);
            }
            else {
                builder.error(CLocalize.lparenthExpected().get());
            }
        }
        else {
            builder.error("'while' expected");
        }

        consumeIf(builder, SEMICOLON);

        marker.done(CElementTypes.DO_WHILE_STATEMENT);
    }

    /**
     * Parses a {@code switch (expr) { … }} statement.
     * The body is a normal code block; {@code case} and {@code default} labels
     * are handled as statements inside that block.
     */
    private static void parseSwitchStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'switch'

        if (builder.getTokenType() == LPARENTH) {
            builder.advanceLexer(); // consume '('
            ExpressionParsing.parseExpression(builder);
            consumeIf(builder, RPARENTH);
        }
        else {
            builder.error(CLocalize.lparenthExpected().get());
        }

        parseCodeBlock(builder); // body: { case x: … default: … }

        marker.done(CElementTypes.SWITCH_STATEMENT);
    }

    /**
     * Parses a {@code case value:} label inside a switch body.
     */
    private static void parseCaseStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'case'

        ExpressionParsing.parseExpression(builder); // constant expression

        consumeIf(builder, COLON);

        marker.done(CElementTypes.CASE_STATEMENT);
    }

    /**
     * Parses a {@code default:} label inside a switch body.
     */
    private static void parseDefaultStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'default'

        consumeIf(builder, COLON);

        marker.done(CElementTypes.DEFAULT_STATEMENT);
    }

    /**
     * Parses a {@code goto label;} statement.
     */
    private static void parseGotoStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'goto'

        if (builder.getTokenType() == IDENTIFIER) {
            builder.advanceLexer(); // label name
        }
        else {
            builder.error("Label name expected");
        }

        consumeIf(builder, SEMICOLON);

        marker.done(CElementTypes.GOTO_STATEMENT);
    }

    /**
     * Parses a labeled statement: {@code label: statement}.
     */
    private static void parseLabeledStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume label name (IDENTIFIER)
        builder.advanceLexer(); // consume ':'

        // Parse the labeled statement if one follows (optional before '}' or EOF)
        if (!builder.eof() && builder.getTokenType() != RBRACE) {
            parseStatement(builder);
        }

        marker.done(CElementTypes.LABELED_STATEMENT);
    }

    private static void parseForStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'for'

        if (builder.getTokenType() == LPARENTH) {
            builder.advanceLexer(); // consume '('

            // init: consume until first ';' (may be a declaration or expression)
            while (!builder.eof() && builder.getTokenType() != SEMICOLON && builder.getTokenType() != RPARENTH) {
                builder.advanceLexer();
            }
            consumeIf(builder, SEMICOLON);

            // condition expression
            if (builder.getTokenType() != SEMICOLON) {
                ExpressionParsing.parseExpression(builder);
            }
            consumeIf(builder, SEMICOLON);

            // update expression
            if (builder.getTokenType() != RPARENTH) {
                ExpressionParsing.parseExpression(builder);
            }

            consumeIf(builder, RPARENTH);
        }
        else {
            builder.error(CLocalize.lparenthExpected().get());
        }

        parseStatement(builder); // body

        marker.done(CElementTypes.FOR_STATEMENT);
    }
}
