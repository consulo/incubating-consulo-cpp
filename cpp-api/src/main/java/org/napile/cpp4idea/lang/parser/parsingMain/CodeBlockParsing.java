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

import consulo.language.parser.PsiBuilder;
import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.lang.psi.*;

/**
 * @author VISTALL
 * @date 10:36/13.12.2011
 */
public class CodeBlockParsing extends MainParsing {
    public static void parseCodeBlock(PsiBuilder builder) {
        PsiBuilder.Marker marker;

        if (builder.getTokenType() != LBRACE) {
            builder.error(CBundle.message("LBRACE.expected"));
            marker = builder.mark();
            builder.advanceLexer();

            done(marker, CPsiParameterList.class);
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
                builder.error(CBundle.message("RBRACE.expected"));
            }
        }

        builder.advanceLexer();

        done(marker, CPsiCodeBlock.class);
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
        else if (builder.getTokenType() == FOR_KEYWORD) {
            parseForStatement(builder);
        }
        else if (builder.getTokenType() == BREAK_KEYWORD) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            consumeIf(builder, SEMICOLON);
            done(marker, CPsiBreakStatement.class);
        }
        else if (builder.getTokenType() == CONTINUE_KEYWORD) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            consumeIf(builder, SEMICOLON);
            done(marker, CPsiContinueStatement.class);
        }
        else if (builder.getTokenType() == LBRACE) {
            parseCodeBlock(builder);
        }
        else {
            builder.advanceLexer();
        }
    }

    private static void parseReturnStatement(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        builder.advanceLexer(); // consume 'return'

        // Only parse an expression when not immediately followed by ';' (void return)
        if (builder.getTokenType() != SEMICOLON) {
            ExpressionParsing.parseExpression(builder);
        }

        if (builder.getTokenType() != SEMICOLON) {
            builder.error(CBundle.message("SEMICOLON.expected"));
        }
        else {
            builder.advanceLexer();
        }

        done(marker, CPsiReturnStatement.class);
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
            builder.error(CBundle.message("LPARENTH.expected"));
        }

        parseStatement(builder); // then-branch

        if (builder.getTokenType() == ELSE_KEYWORD) {
            builder.advanceLexer(); // consume 'else'
            parseStatement(builder); // else-branch
        }

        done(marker, CPsiIfStatement.class);
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
            builder.error(CBundle.message("LPARENTH.expected"));
        }

        parseStatement(builder); // body

        done(marker, CPsiWhileStatement.class);
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
            builder.error(CBundle.message("LPARENTH.expected"));
        }

        parseStatement(builder); // body

        done(marker, CPsiForStatement.class);
    }
}
