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
import consulo.language.ast.TokenSet;
import consulo.language.parser.PsiBuilder;

/**
 * C and C++ expressions, by precedence: comma, assignment, conditional, the binary operators, cast, unary, postfix,
 * primary.
 *
 * @author VISTALL
 * @since 2026-10-19
 */
public class CExpressionParsing extends CParsingBase {
    /**
     * Binary operators, loosest binding first.
     */
    private static final TokenSet[] BINARY_LEVELS = {
        TokenSet.create(OROR),
        TokenSet.create(ANDAND),
        TokenSet.create(OR),
        TokenSet.create(XOR),
        TokenSet.create(AND),
        EQUALITY_OPERATIONS,
        RELATIONAL_OPERATIONS,
        SHIFT_OPERATIONS,
        ADDITIVE_OPERATIONS,
        MULTIPLICATIVE_OPERATIONS
    };

    /**
     * Tokens which can start the operand of a cast whatever the parenthesised name is - none of them could continue
     * a parenthesised expression instead.
     */
    private static final TokenSet UNAMBIGUOUS_OPERAND_STARTS = TokenSet.orSet(
        TokenSet.create(IDENTIFIER, LPARENTH, EXCL, TILDE, SIZEOF_KEYWORD, ALIGNOF_KEYWORD, THIS_KEYWORD, NEW_KEYWORD,
            DELETE_KEYWORD, CAST_KEYWORD),
        LITERAL_EXPRESSION_SET
    );

    /**
     * Tokens which can start the operand of a cast only when the parenthesised name is known to be a type:
     * {@code (x) - 1} subtracts, {@code (int) -1} casts.
     */
    private static final TokenSet AMBIGUOUS_OPERAND_STARTS = TokenSet.create(PLUS, MINUS, ASTERISK, AND, PLUSPLUS, MINUSMINUS, DOUBLE_COLON);

    /**
     * What may follow a name continuing a string literal: another literal, or the end of the operand.
     */
    private static final TokenSet STRING_MACRO_FOLLOWERS = TokenSet.create(STRING_LITERAL, RPARENTH, COMMA, SEMICOLON, RBRACKET, RBRACE, COLON);

    public CExpressionParsing(CParsingContext context) {
        super(context);
    }

    /**
     * {@code assignment (, assignment)*}
     */
    public boolean parseExpression() {
        PsiBuilder.Marker marker = mark();
        if (!parseAssignmentExpression()) {
            marker.drop();
            return false;
        }

        while (at(COMMA)) {
            advance();
            if (!parseAssignmentExpression()) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.COMMA_EXPRESSION);
            marker = marker.precede();
        }
        marker.drop();
        return true;
    }

    public void parseExpressionOrError() {
        if (!parseExpression()) {
            error(CLocalize.expressionExpected());
        }
    }

    public boolean parseAssignmentExpression() {
        if (at(THROW_KEYWORD)) {
            PsiBuilder.Marker marker = mark();
            advance();
            if (!at(SEMICOLON) && !at(RPARENTH) && !at(COMMA)) {
                parseAssignmentExpression();
            }
            marker.done(CElementTypes.PREFIX_EXPRESSION);
            return true;
        }

        PsiBuilder.Marker marker = mark();
        if (!parseConditionalExpression()) {
            marker.drop();
            return false;
        }

        if (at(ASSIGNMENT_OPERATIONS)) {
            advance();
            if (!parseInitializer()) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.ASSIGNMENT_EXPRESSION);
        }
        else {
            marker.drop();
        }
        return true;
    }

    public void parseAssignmentExpressionOrError() {
        if (!parseAssignmentExpression()) {
            error(CLocalize.expressionExpected());
        }
    }

    public boolean parseConditionalExpression() {
        PsiBuilder.Marker marker = mark();
        if (!parseBinaryExpression(0)) {
            marker.drop();
            return false;
        }

        if (at(QUEST)) {
            advance();
            // GNU: a ?: b
            if (!at(COLON)) {
                parseExpressionOrError();
            }
            expect(COLON, CLocalize.colonExpected());
            parseAssignmentExpressionOrError();
            marker.done(CElementTypes.CONDITIONAL_EXPRESSION);
        }
        else {
            marker.drop();
        }
        return true;
    }

    public void parseConditionalExpressionOrError() {
        if (!parseConditionalExpression()) {
            error(CLocalize.expressionExpected());
        }
    }

    private boolean parseBinaryExpression(int level) {
        if (level == BINARY_LEVELS.length) {
            return parseCastExpression();
        }

        PsiBuilder.Marker marker = mark();
        if (!parseBinaryExpression(level + 1)) {
            marker.drop();
            return false;
        }

        while (at(BINARY_LEVELS[level])) {
            advance();
            if (!parseBinaryExpression(level + 1)) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.BINARY_EXPRESSION);
            marker = marker.precede();
        }
        marker.drop();
        return true;
    }

    public boolean parseCastExpression() {
        if (at(LPARENTH) && isCast()) {
            PsiBuilder.Marker marker = mark();
            advance();
            declarations().parseTypeName();
            expect(RPARENTH, CLocalize.rparenthExpected());

            // a compound literal, (struct point) { 1, 2 }
            if (at(LBRACE)) {
                parseInitializerList();
            }
            else if (!parseCastExpression()) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.CAST_EXPRESSION);
            return true;
        }
        return parseUnaryExpression();
    }

    /**
     * At {@code (}: whether a cast follows rather than a parenthesised expression. Decided by reading ahead: the
     * parentheses must hold a type name, and what follows must be able to start the operand.
     */
    private boolean isCast() {
        PsiBuilder.Marker speculation = mark();
        try {
            advance();
            CDeclarationParsing.TypeName typeName = declarations().parseTypeName();
            if (typeName == CDeclarationParsing.TypeName.NONE || !at(RPARENTH)) {
                return false;
            }
            advance();

            if (typeName == CDeclarationParsing.TypeName.UNKNOWN_NAME) {
                return at(UNAMBIGUOUS_OPERAND_STARTS);
            }
            return at(LBRACE) || at(UNAMBIGUOUS_OPERAND_STARTS) || at(AMBIGUOUS_OPERAND_STARTS);
        }
        finally {
            speculation.rollbackTo();
        }
    }

    public boolean parseUnaryExpression() {
        IElementType type = token();

        if (type == SIZEOF_KEYWORD || type == ALIGNOF_KEYWORD) {
            PsiBuilder.Marker marker = mark();
            advance();
            if (at(ELLIPSIS)) {
                // sizeof...(pack)
                advance();
            }
            if (at(LPARENTH) && isParenthesizedTypeName()) {
                advance();
                declarations().parseTypeName();
                expect(RPARENTH, CLocalize.rparenthExpected());
            }
            else if (!parseUnaryExpression()) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.SIZEOF_EXPRESSION);
            return true;
        }

        if (at(UNARY_OPERATIONS) || type == ANDAND) {
            // ANDAND: GNU address of a label, &&label
            PsiBuilder.Marker marker = mark();
            advance();
            if (!parseCastExpression()) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.PREFIX_EXPRESSION);
            return true;
        }

        if (type == EXTENSION_KEYWORD) {
            PsiBuilder.Marker marker = mark();
            advance();
            if (!parseCastExpression()) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.PREFIX_EXPRESSION);
            return true;
        }

        if (type == NEW_KEYWORD || type == DOUBLE_COLON && lookAhead(1) == NEW_KEYWORD) {
            parseNewExpression();
            return true;
        }

        if (type == DELETE_KEYWORD || type == DOUBLE_COLON && lookAhead(1) == DELETE_KEYWORD) {
            PsiBuilder.Marker marker = mark();
            consume(DOUBLE_COLON);
            advance();
            if (at(LBRACKET) && lookAhead(1) == RBRACKET) {
                advance();
                advance();
            }
            if (!parseCastExpression()) {
                error(CLocalize.expressionExpected());
            }
            marker.done(CElementTypes.PREFIX_EXPRESSION);
            return true;
        }

        return parsePostfixExpression();
    }

    private boolean isParenthesizedTypeName() {
        PsiBuilder.Marker speculation = mark();
        try {
            advance();
            CDeclarationParsing.TypeName typeName = declarations().parseTypeName();
            return typeName != CDeclarationParsing.TypeName.NONE && typeName != CDeclarationParsing.TypeName.UNKNOWN_NAME && at(RPARENTH);
        }
        finally {
            speculation.rollbackTo();
        }
    }

    private void parseNewExpression() {
        PsiBuilder.Marker marker = mark();
        consume(DOUBLE_COLON);
        advance();

        // placement arguments: new (buffer) T
        if (at(LPARENTH)) {
            parseArgumentList();
        }

        declarations().parseTypeName();

        if (at(LPARENTH)) {
            parseArgumentList();
        }
        else if (at(LBRACE)) {
            parseInitializerList();
        }
        marker.done(CElementTypes.NEW_EXPRESSION);
    }

    public boolean parsePostfixExpression() {
        PsiBuilder.Marker marker = mark();
        if (!parsePrimaryExpression()) {
            marker.drop();
            return false;
        }

        while (true) {
            if (at(LBRACKET)) {
                advance();
                if (isCpp() && at(LBRACE)) {
                    parseInitializerList();
                }
                else {
                    parseExpressionOrError();
                }
                expect(RBRACKET, CLocalize.rbracketExpected());
                marker.done(CElementTypes.INDEXED_PROPERTY_ACCESS_EXPRESSION);
            }
            else if (at(LPARENTH)) {
                parseArgumentList();
                marker.done(CElementTypes.CALL_EXPRESSION);
            }
            else if (at(DOT) || at(ARROW)) {
                advance();
                consume(TEMPLATE_KEYWORD);
                consume(TILDE);
                expect(IDENTIFIER, CLocalize.nameExpected());
                marker.done(CElementTypes.REFERENCE_EXPRESSION);
            }
            else if (at(PLUSPLUS) || at(MINUSMINUS)) {
                advance();
                marker.done(CElementTypes.POSTFIX_EXPRESSION);
            }
            else {
                break;
            }
            marker = marker.precede();
        }
        marker.drop();
        return true;
    }

    public boolean parsePrimaryExpression() {
        IElementType type = token();

        if (type == IDENTIFIER || isCpp() && type == DOUBLE_COLON && lookAhead(1) == IDENTIFIER) {
            parseReference();
            return true;
        }

        if (type == STRING_LITERAL) {
            // adjacent string literals are one string; a name among them can only be a string macro whose definition
            // is not visible - "version " VERSION "\n"
            PsiBuilder.Marker marker = mark();
            while (at(STRING_LITERAL) || at(IDENTIFIER) && STRING_MACRO_FOLLOWERS.contains(lookAhead(1))) {
                advance();
            }
            marker.done(CElementTypes.LITERAL_EXPRESSION);
            return true;
        }

        if (at(LITERAL_EXPRESSION_SET)) {
            PsiBuilder.Marker marker = mark();
            advance();
            marker.done(CElementTypes.LITERAL_EXPRESSION);
            return true;
        }

        if (type == THIS_KEYWORD) {
            PsiBuilder.Marker marker = mark();
            advance();
            marker.done(CElementTypes.REFERENCE_EXPRESSION);
            return true;
        }

        if (type == LPARENTH) {
            PsiBuilder.Marker marker = mark();
            advance();
            if (at(LBRACE)) {
                // GNU statement expression, ({ ... })
                statements().parseCodeBlock();
            }
            else {
                parseExpressionOrError();
            }
            expect(RPARENTH, CLocalize.rparenthExpected());
            marker.done(CElementTypes.PARENTHESIZED_EXPRESSION);
            return true;
        }

        if (type == CAST_KEYWORD) {
            // static_cast<T>(e)
            PsiBuilder.Marker marker = mark();
            advance();
            if (expect(LT, CLocalize.ltExpected())) {
                declarations().parseTypeName();
                expect(GT, CLocalize.gtExpected());
            }
            if (at(LPARENTH)) {
                advance();
                parseExpressionOrError();
                expect(RPARENTH, CLocalize.rparenthExpected());
            }
            marker.done(CElementTypes.CAST_EXPRESSION);
            return true;
        }

        if (type == GENERIC_KEYWORD || type == TYPEID_KEYWORD || type == NOEXCEPT_KEYWORD || type == DECLTYPE_KEYWORD) {
            // _Generic(e, int: a, default: b), typeid(x), noexcept(e): parsed for their extent only
            PsiBuilder.Marker marker = mark();
            advance();
            if (at(LPARENTH)) {
                skipBalanced();
            }
            marker.done(CElementTypes.CALL_EXPRESSION);
            return true;
        }

        if (isCpp() && type == LBRACE) {
            parseInitializerList();
            return true;
        }

        if (isCpp() && type == LBRACKET) {
            return parseLambda();
        }

        if (isCpp() && at(TYPES) && (lookAhead(1) == LPARENTH || lookAhead(1) == LBRACE)) {
            // functional cast, int(x)
            PsiBuilder.Marker marker = mark();
            advance();
            if (at(LPARENTH)) {
                parseArgumentList();
            }
            else {
                parseInitializerList();
            }
            marker.done(CElementTypes.CAST_EXPRESSION);
            return true;
        }

        return false;
    }

    /**
     * A name, qualified in C++: {@code a}, {@code ::a}, {@code ns::a::b}.
     */
    private void parseReference() {
        PsiBuilder.Marker marker = mark();
        consume(DOUBLE_COLON);
        advance();
        marker.done(CElementTypes.REFERENCE_EXPRESSION);

        while (isCpp() && at(DOUBLE_COLON) && (lookAhead(1) == IDENTIFIER || lookAhead(1) == TILDE)) {
            marker = marker.precede();
            advance();
            consume(TILDE);
            advance();
            marker.done(CElementTypes.REFERENCE_EXPRESSION);
        }
    }

    /**
     * {@code [captures] (parameters) specifiers -> type { body }}
     */
    private boolean parseLambda() {
        PsiBuilder.Marker marker = mark();
        skipBalanced();
        if (at(LPARENTH)) {
            declarations().parseParameterList();
        }
        while (at(MODIFIERS) || at(NOEXCEPT_KEYWORD) || at(IDENTIFIER)) {
            // mutable, constexpr, noexcept
            advance();
        }
        if (consume(ARROW)) {
            declarations().parseTypeName();
        }
        if (!at(LBRACE)) {
            marker.rollbackTo();
            return false;
        }
        statements().parseCodeBlock();
        marker.done(CElementTypes.CALL_EXPRESSION);
        return true;
    }

    public void parseArgumentList() {
        PsiBuilder.Marker marker = mark();
        advance();

        if (!at(RPARENTH)) {
            while (true) {
                if (!parseArgument()) {
                    error(CLocalize.expressionExpected());
                    if (!at(COMMA)) {
                        break;
                    }
                }
                if (!consume(COMMA)) {
                    break;
                }
            }
        }

        expect(RPARENTH, CLocalize.rparenthExpected());
        marker.done(CElementTypes.ARGUMENT_LIST);
    }

    /**
     * An argument, or a type name - which a macro such as {@code va_arg(ap, int)} or {@code offsetof(struct s, f)}
     * takes, and which stays one when the macro is not known.
     */
    private boolean parseArgument() {
        if (declarations().startsTypeNameDefinitely()) {
            declarations().parseTypeName();
            return true;
        }
        if (isCpp() && at(LBRACE)) {
            parseInitializerList();
            return true;
        }
        return parseAssignmentExpression();
    }

    /**
     * An initializer: a braced list, or an assignment expression.
     */
    public boolean parseInitializer() {
        if (at(LBRACE)) {
            parseInitializerList();
            return true;
        }
        return parseAssignmentExpression();
    }

    /**
     * {@code { initializer, .field = initializer, [index] = initializer, }}
     */
    public void parseInitializerList() {
        PsiBuilder.Marker marker = mark();
        advance();

        while (!eof() && !at(RBRACE)) {
            boolean designated = false;
            while (at(DOT) && lookAhead(1) == IDENTIFIER || at(LBRACKET)) {
                if (at(DOT)) {
                    advance();
                    advance();
                }
                else {
                    advance();
                    parseConditionalExpressionOrError();
                    // GNU range, [1 ... 5]
                    if (consume(ELLIPSIS)) {
                        parseConditionalExpressionOrError();
                    }
                    expect(RBRACKET, CLocalize.rbracketExpected());
                }
                designated = true;
            }

            if (designated) {
                expect(EQ, CLocalize.eqExpected());
            }
            else if (at(IDENTIFIER) && lookAhead(1) == COLON) {
                // old GNU designator, field: value
                advance();
                advance();
            }

            if (!parseInitializer()) {
                skipUntil(TokenSet.create(COMMA, RBRACE, SEMICOLON), CLocalize.expressionExpected());
            }
            consume(ELLIPSIS);

            if (!consume(COMMA)) {
                break;
            }
        }

        expect(RBRACE, CLocalize.rbraceExpected());
        marker.done(CElementTypes.INITIALIZER_LIST);
    }
}
