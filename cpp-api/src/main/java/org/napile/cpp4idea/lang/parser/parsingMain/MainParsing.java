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
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CElementTypes;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 14:26/11.12.2011
 */
public class MainParsing extends MainParserHelper {

    public static void parseElement(@NotNull PsiBuilder builder) {
        while (!builder.eof()) {
            PsiBuilder.Marker marker = builder.mark();

            IElementType doneElement = parse(builder);
            if (doneElement != null) {
                marker.done(doneElement);
            }
            else {
                builder.advanceLexer();
                marker.error("Unexpected element");
            }
        }
    }

    public static boolean parseTypeRef(PsiBuilder builder) {
        PsiBuilder.Marker marker = parseTypeRef0(builder);
        if (marker == null) {
            return false;
        }
        if (builder.getTokenType() == DOUBLE_COLON) {
            marker = marker.precede();

            builder.advanceLexer();

            if (parseTypeRef0(builder) == null) {
                error(builder, CLocalize.identifierExpected());
            }

            marker.done(CElementTypes.DOUBLE_COLON_TYPE_REF);

            return true;
        }
        else {
            return true;
        }
    }

    public static PsiBuilder.Marker parseTypeRef0(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        consumeIf(builder, UNSIGNED_KEYWORD);

        consumeIf(builder, SIGNED_KEYWORD);

        IElementType currentToken = builder.getTokenType();
        if (CPsiTokens.AGGREGATE_TYPES.contains(currentToken)) {
            // struct Foo or union Bar used as a type in a declaration
            builder.advanceLexer(); // consume 'struct'/'union'
            consumeIf(builder, IDENTIFIER); // optional tag name

            consumeIf(builder, ASTERISK);

            marker.done(CElementTypes.TYPE_REF);
            return marker;
        }

        if (!CPsiTokens.TYPES.contains(currentToken) && currentToken != IDENTIFIER) {
            marker.drop();
            return null;
        }

        builder.advanceLexer();

        consumeIf(builder, ASTERISK);

        marker.done(CElementTypes.TYPE_REF);

        return marker;
    }

    public static IElementType parse(@NotNull PsiBuilder builder) {
        IElementType token = builder.getTokenType();

        if (token == CPsiTokens.CLASS_KEYWORD) {
            return parseClass(builder);
        }
        else if (token == CPsiTokens.ENUM_KEYWORD) {
            return parseEnum(builder, true);
        }
        else if (token == CPsiTokens.NAMESPACE_KEYWORD) {
            return parseNamespace(builder);
        }
        else if (token == CPsiTokens.TYPEDEF_KEYWORD) {
            return parseTypeDef(builder);
        }
        else if (token == CPsiTokens.STRUCT_KEYWORD) {
            return parseStruct(builder);
        }
        else if (token == CPsiTokens.UNION_KEYWORD) {
            return parseUnion(builder);
        }

        return parseMethodOrField(builder);
    }

    private static IElementType parseTypeDef(PsiBuilder builder) {
        advanceLexerAndSkipLines(builder);

        parseTypeRef(builder);  // type

        parseTypeRef(builder);  // new type name

        expect(builder, SEMICOLON, CLocalize.semicolonExpected());

        return CElementTypes.TYPE_DECLARATION;
    }

    /**
     * Parses a top-level {@code struct Name { … };} declaration.
     */
    private static IElementType parseStruct(@NotNull PsiBuilder builder) {
        return parseAggregateDeclaration(builder, CElementTypes.STRUCT_DECLARATION);
    }

    /**
     * Parses a top-level {@code union Name { … };} declaration.
     */
    private static IElementType parseUnion(@NotNull PsiBuilder builder) {
        return parseAggregateDeclaration(builder, CElementTypes.UNION_DECLARATION);
    }

    /**
     * Shared body for {@code struct} and {@code union} top-level declarations.
     * Grammar: keyword [Name] { memberDecl* } ;
     */
    private static IElementType parseAggregateDeclaration(
            @NotNull PsiBuilder builder, IElementType doneType) {
        builder.advanceLexer(); // consume 'struct' or 'union'

        consumeIf(builder, IDENTIFIER); // optional tag name

        if (consumeIf(builder, LBRACE)) {
            while (!builder.eof() && builder.getTokenType() != RBRACE) {
                PsiBuilder.Marker memberMarker = builder.mark();
                IElementType member = parseMemberDeclaration(builder);
                if (member != null) {
                    memberMarker.done(member);
                }
                else {
                    memberMarker.drop();
                    // skip unrecognized tokens to prevent infinite loop
                    builder.error("Unexpected token in struct/union body");
                    builder.advanceLexer();
                }
            }

            expect(builder, RBRACE, CLocalize.rbraceExpected());
            consumeIf(builder, SEMICOLON);
        }

        return doneType;
    }

    private static IElementType parseNamespace(@NotNull PsiBuilder builder) {
        builder.advanceLexer();

        expect(builder, IDENTIFIER, CLocalize.nameExpected());

        expect(builder, LBRACE, CLocalize.lbraceExpected());

        while (!builder.eof()) {
            PsiBuilder.Marker marker = builder.mark();
            IElementType child = parse(builder);
            if (child != null) {
                marker.done(child);
            }
            else {
                marker.drop();
                break;
            }
        }

        expect(builder, RBRACE, CLocalize.rbraceExpected());

        return CElementTypes.NAMESPACE_DECLARATION;
    }

    private static IElementType parseEnum(@NotNull PsiBuilder builder, boolean semicolon) {
        builder.advanceLexer();

        consumeIf(builder, IDENTIFIER);  //name

        if (builder.getTokenType() == LBRACE) {
            builder.advanceLexer();

            while (builder.getTokenType() != RBRACE) {
                if (builder.getTokenType() == IDENTIFIER) {
                    PsiBuilder.Marker constantBuilder = builder.mark();

                    builder.advanceLexer();
                    if (builder.getTokenType() == EQ) {
                        ExpressionParsing.parse(builder);
                    }

                    constantBuilder.done(CElementTypes.ENUM_CONSTANT);

                    if (builder.getTokenType() == COMMA) {
                        builder.advanceLexer();
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
            }

            expect(builder, RBRACE, CLocalize.rbraceExpected());

            if (semicolon) {
                expect(builder, SEMICOLON, CLocalize.semicolonExpected());
            }
        }

        return CElementTypes.ENUM;
    }

    private static IElementType parseClass(@NotNull PsiBuilder builder) {
        builder.advanceLexer();

        expect(builder, IDENTIFIER, CLocalize.nameExpected());

        if (consumeIf(builder, SEMICOLON)) {
            return CElementTypes.CLASS;
        }

        if (builder.getTokenType() == COLON) {
            PsiBuilder.Marker superListMarker = builder.mark();

            builder.advanceLexer();

            while (true) {
                PsiBuilder.Marker superMarker = builder.mark();

                if (ACCESS_MODIFIERS.contains(builder.getTokenType())) {
                    builder.advanceLexer();
                }

                if (consumeIf(builder, IDENTIFIER)) {
                    superMarker.done(CElementTypes.SUPER_CLASS);
                }
                else {
                    superMarker.done(CElementTypes.SUPER_CLASS);
                    error(builder, CLocalize.identifierExpected());
                }

                if (!consumeIf(builder, COMMA)) {
                    break;
                }
            }
            superListMarker.done(CElementTypes.SUPER_CLASS_LIST);
        }

        if (consumeIf(builder, LBRACE)) {
            while (!builder.eof()) {
                IElementType classToken = builder.getTokenType();
                if (classToken == RBRACE) {
                    break;
                }

                if (CPsiTokens.ACCESS_MODIFIERS.contains(classToken)) {
                    // public: or private: section — group following members under ACCESSOR_OWNER
                    PsiBuilder.Marker memberMark = builder.mark();
                    builder.advanceLexer(); // consume access modifier

                    if (builder.getTokenType() == COLON) {
                        builder.advanceLexer(); // consume ':'

                        while (true) {
                            // Stop if we hit the end, closing brace, or another access modifier
                            IElementType next = builder.getTokenType();
                            if (next == RBRACE || CPsiTokens.ACCESS_MODIFIERS.contains(next)) {
                                break;
                            }

                            PsiBuilder.Marker marker = builder.mark();
                            IElementType element = parseMemberDeclaration(builder);
                            if (element != null) {
                                marker.done(element);
                            }
                            else {
                                marker.drop();
                                break;
                            }
                        }

                        memberMark.done(CElementTypes.ACCESSOR_OWNER);
                    }
                    else {
                        memberMark.drop();
                        error(builder, CLocalize.colonExpected());
                    }
                }
                else {
                    // Member without an explicit access specifier (allowed in C++ classes and common in practice)
                    PsiBuilder.Marker marker = builder.mark();
                    IElementType element = parseMemberDeclaration(builder);
                    if (element != null) {
                        marker.done(element);
                    }
                    else {
                        marker.drop();
                        builder.error("Unexpected token in class body");
                        builder.advanceLexer();
                    }
                }
            }

            expect(builder, RBRACE, CLocalize.rbraceExpected());

            consumeIf(builder, SEMICOLON);
        }

        return CElementTypes.CLASS;
    }

    private static IElementType parseMemberDeclaration(@NotNull PsiBuilder builder) {
        parseModifiers(builder);

        if (consumeIf(builder, TILDE)) {
            if (!parseTypeRef(builder)) {
                error(builder, CLocalize.identifierExpected());
                return CElementTypes.DECLARATION_CONSTRUCTOR;
            }

            ParameterListParsing.parseParameterList(builder);

            expect(builder, SEMICOLON, CLocalize.semicolonExpected());

            return CElementTypes.DECLARATION_CONSTRUCTOR;
        }

        if (!parseTypeRef(builder)) {
            return null;
        }

        if (builder.getTokenType() == LPARENTH) {
            ParameterListParsing.parseParameterList(builder);

            expect(builder, SEMICOLON, CLocalize.semicolonExpected());

            return CElementTypes.DECLARATION_CONSTRUCTOR;
        }
        else if (builder.getTokenType() == IDENTIFIER) {
            builder.advanceLexer(); // consume member name

            if (builder.getTokenType() == LPARENTH) {
                // method declaration: TypeRef name(params);
                ParameterListParsing.parseParameterList(builder);
                expect(builder, SEMICOLON, CLocalize.semicolonExpected());
                return CElementTypes.DECLARATION_METHOD;
            }
            else {
                // field declaration: TypeRef name[...] [= expr];
                while (consumeIf(builder, LBRACKET)) {
                    if (builder.getTokenType() != RBRACKET) {
                        ExpressionParsing.parseExpression(builder);
                    }
                    consumeIf(builder, RBRACKET);
                }
                if (consumeIf(builder, EQ)) {
                    ExpressionParsing.parseExpression(builder);
                }
                consumeIf(builder, SEMICOLON);
                return CElementTypes.DECLARATION_FIELD;
            }
        }

        if (builder.getTokenType() == ASTERISK) {
            builder.advanceLexer();

            expect(builder, IDENTIFIER, CLocalize.identifierExpected());

            while (consumeIf(builder, LBRACKET)) {
                if (!consumeIf(builder, RBRACKET)) {
                    error(builder, CLocalize.rbracketExpected());
                    break;
                }
            }
            expect(builder, SEMICOLON, CLocalize.semicolonExpected());
            return CElementTypes.DECLARATION_FIELD;
        }

        return null;
    }

    public static IElementType parseMethodOrField(@NotNull PsiBuilder builder) {
        parseModifiers(builder);

        if (!parseTypeRef(builder)) {
            return null;
        }

        if (builder.getTokenType() == DOUBLE_COLON) {
            /*boolean deConstructor = */
            consumeIf(builder, TILDE);

            parseTypeRef(builder);

            ParameterListParsing.parseParameterList(builder);

            if (builder.getTokenType() != SEMICOLON) {
                CodeBlockParsing.parseCodeBlock(builder);

                return CElementTypes.IMPLEMENTING_METHOD;
            }
            else {
                builder.advanceLexer();

                return CElementTypes.DECLARATION_METHOD;
            }
        }

        if (builder.getTokenType() != IDENTIFIER) {
            error(builder, CLocalize.nameExpected());
            return null;
        }
        else {
            advanceLexerAndSkipLines(builder);
        }

        if (builder.getTokenType() == LPARENTH) {
            ParameterListParsing.parseParameterList(builder);

            if (builder.getTokenType() != SEMICOLON) {
                CodeBlockParsing.parseCodeBlock(builder);

                return CElementTypes.IMPLEMENTING_METHOD;
            }
            else {
                builder.advanceLexer();

                return CElementTypes.DECLARATION_METHOD;
            }
        }
        else {
            if (builder.getTokenType() != SEMICOLON) {
                if (builder.getTokenType() != EQ) {
                    builder.error(CLocalize.eqExpected().get());
                }
                else {
                    advanceLexerAndSkipLines(builder);

                    ExpressionParsing.parseExpression(builder);

                    if (builder.getTokenType() != SEMICOLON) {
                        builder.error(CLocalize.semicolonExpected().get());
                    }

                    builder.advanceLexer();
                }
            }
            else {
                builder.advanceLexer();
            }

            return CElementTypes.FIELD;
        }
    }

    private static void parseModifiers(@NotNull PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        while (!builder.eof() && CPsiTokens.MODIFIERS.contains(builder.getTokenType())) {
            advanceLexerAndSkipLines(builder);
        }

        marker.done(CElementTypes.MODIFIER_LIST);
    }
}
