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

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.lang.psi.*;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 14:26/11.12.2011
 */
public class MainParsing extends MainParserHelper {
	public static final int SILENT = 1 << 0;

	public static void parseElement(@NotNull PsiBuilder builder, int flags) {
		while (!builder.eof()) {
			skipLines(builder);

			PsiBuilder.Marker marker = builder.mark();

			Class<? extends CPsiElement> doneElement = parse(builder);
			if (doneElement != null) {
				done(marker, doneElement);
			}
			else {
				marker.drop();
				if (!isSet(flags, SILENT)) {
					builder.error("Unknown how to parse symbol " + builder.getTokenType());
					builder.advanceLexer();
				}
				else {
					break;
				}
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
				error(builder, "IDENTIFIER.expected");
			}

			done(marker, CPsiDoubleColonTypeRef.class);

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
		if (!CPsiTokens.TYPES.contains(currentToken) && currentToken != IDENTIFIER) {
			marker.drop();
			return null;
		}

		builder.advanceLexer();

		consumeIf(builder, ASTERISK);

		done(marker, CPsiTypeRef.class);

		return marker;
	}

	public static Class<? extends CPsiElement> parse(@NotNull PsiBuilder builder) {
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

		return parseMethodOrField(builder);
	}

	private static Class<? extends CPsiElement> parseTypeDef(PsiBuilder builder) {
		advanceLexerAndSkipLines(builder);

		parseTypeRef(builder);  // type

		parseTypeRef(builder);  // new type name

		expect(builder, SEMICOLON, "SEMICOLON.expected");

		return CPsiTypeDeclaration.class;
	}

	private static Class<? extends CPsiElement> parseNamespace(@NotNull PsiBuilder builder) {
		builder.advanceLexer();

		expect(builder, IDENTIFIER, "name.expected");

		expect(builder, LBRACE, "LBRACE.expected");

		while (!builder.eof()) {
			PsiBuilder.Marker marker = builder.mark();
			Class<? extends CPsiElement> child = parse(builder);
			if (child != null) {
				done(marker, child);
			}
			else {
				marker.drop();
				break;
			}
		}

		expect(builder, RBRACE, "RBRACE.expected");

		return CPsiNamespaceDeclaration.class;
	}

	private static Class<? extends CPsiElement> parseEnum(@NotNull PsiBuilder builder, boolean semicolon) {
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

					done(constantBuilder, CPsiEnumConstant.class);

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

			expect(builder, RBRACE, "RBRACE.expected");

			if (semicolon) {
				expect(builder, SEMICOLON, "SEMICOLON.expected");
			}
		}

		return CPsiEnum.class;
	}

	private static Class<? extends CPsiElement> parseClass(@NotNull PsiBuilder builder) {
		builder.advanceLexer();

		expect(builder, IDENTIFIER, "name.expected");

		if (consumeIf(builder, SEMICOLON)) {
			return CPsiClass.class;
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
					done(superMarker, CPsiSuperClass.class);
				}
				else {
					done(superMarker, CPsiSuperClass.class);
					error(builder, "IDENTIFIER.expected");
				}

				if (!consumeIf(builder, COMMA)) {
					break;
				}
			}
			done(superListMarker, CPsiSuperClassList.class);
		}

		if (consumeIf(builder, LBRACE)) {
			while (!builder.eof()) {
				IElementType classToken = builder.getTokenType();
				if (classToken == RBRACE) {
					break;
				}

				if (CPsiTokens.ACCESS_MODIFIERS.contains(classToken)) {
					PsiBuilder.Marker memberMark = builder.mark();
					builder.advanceLexer();

					if (builder.getTokenType() == COLON) {
						builder.advanceLexer();

						while (true) {
							PsiBuilder.Marker marker = builder.mark();

							Class<? extends CPsiElement> element = parseMemberDeclaration(builder);
							if (element != null) {
								done(marker, element);
							}
							else {
								marker.drop();
								break;
							}
						}

						done(memberMark, CPsiAccessorOwner.class);
					}
					else {
						memberMark.drop();
						error(builder, "COLON.expected");
					}
				}
				else {
					builder.error("Unknown symbol");
					break;
				}
			}

			expect(builder, RBRACE, "RBRACE.expected");

			consumeIf(builder, SEMICOLON);
		}

		return CPsiClass.class;
	}

	private static Class<? extends CPsiElement> parseMemberDeclaration(@NotNull PsiBuilder builder) {
		parseModifiers(builder);

		if (consumeIf(builder, TILDE)) {
			if (!parseTypeRef(builder)) {
				error(builder, "IDENTIFIER.expected");
				return CPsiDeclarationConstructor.class;
			}

			ParameterListParsing.parseParameterList(builder);

			expect(builder, SEMICOLON, "SEMICOLON.expected");

			return CPsiDeclarationConstructor.class;
		}

		if (!parseTypeRef(builder)) {
			return null;
		}

		if (builder.getTokenType() == LPARENTH) {
			ParameterListParsing.parseParameterList(builder);

			expect(builder, SEMICOLON, "SEMICOLON.expected");

			return CPsiDeclarationConstructor.class;
		}
		else if (builder.getTokenType() == IDENTIFIER) {
			builder.advanceLexer();

			ParameterListParsing.parseParameterList(builder);

			expect(builder, SEMICOLON, "SEMICOLON.expected");

			return CPsiDeclarationMethod.class;
		}

		if (builder.getTokenType() == ASTERISK) {
			builder.advanceLexer();

			expect(builder, IDENTIFIER, "IDENTIFIER.expected");

			while (consumeIf(builder, LBRACKET)) {
				if (!consumeIf(builder, RBRACKET)) {
					error(builder, "RBRACKET.expected");
					break;
				}
			}
			expect(builder, SEMICOLON, "SEMICOLON.expected");
			return CPsiDeclarationField.class;
		}

		return null;
	}

	public static Class<? extends CPsiElement> parseMethodOrField(@NotNull PsiBuilder builder) {
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

				return CPsiImplementingMethod.class;
			}
			else {
				builder.advanceLexer();

				return CPsiDeclarationMethod.class;
			}
		}

		if (builder.getTokenType() != IDENTIFIER) {
			error(builder, "name.expected");
			return null;
		}
		else {
			advanceLexerAndSkipLines(builder);
		}

		if (builder.getTokenType() == LPARENTH) {
			ParameterListParsing.parseParameterList(builder);

			if (builder.getTokenType() != SEMICOLON) {
				CodeBlockParsing.parseCodeBlock(builder);

				return CPsiImplementingMethod.class;
			}
			else {
				builder.advanceLexer();

				return CPsiDeclarationMethod.class;
			}
		}
		else {
			if (builder.getTokenType() != SEMICOLON) {
				if (builder.getTokenType() != EQ) {
					builder.error(CBundle.message("EQ.expected"));
				}
				else {
					advanceLexerAndSkipLines(builder);

					ExpressionParsing.parseExpression(builder);

					if (builder.getTokenType() != SEMICOLON) {
						builder.error(CBundle.message("SEMICOLON.expected"));
					}

					builder.advanceLexer();
				}
			}
			else {
				builder.advanceLexer();
			}

			return CPsiField.class;
		}
	}

	private static void parseModifiers(@NotNull PsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();

		while (!builder.eof() && CPsiTokens.MODIFIERS.contains(builder.getTokenType()))
			advanceLexerAndSkipLines(builder);

		done(marker, CPsiModifierList.class);
	}
}
