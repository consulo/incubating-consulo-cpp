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
import org.napile.cpp4idea.lang.psi.CPsiDeclarationMethod;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import org.napile.cpp4idea.lang.psi.CPsiField;
import org.napile.cpp4idea.lang.psi.CPsiImplementingMethod;
import org.napile.cpp4idea.lang.psi.CPsiTypeRef;
import org.napile.cpp4idea.lang.psi.CTokens;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 14:26/11.12.2011
 */
public class MainParsing extends MainParserHelper
{
	public static void parseElement(@NotNull PsiBuilder builder, int flags)
	{
		while(!builder.eof())
		{
			skipLines(builder);

			if(CTokens.TYPES.contains(builder.getTokenType()) || builder.getTokenType() == CTokens.IDENTIFIER)
			{
				parseMethodOrField(builder);
			}
			else
			{
				builder.error("Unknown how to parse symbol " + builder.getTokenType());
				builder.advanceLexer();
			}
		}
	}

/*	public static void parseElement(@NotNull PsiBuilder builder, int flags)
	{
		while(!builder.eof())
		{
			skipLines(builder);

			if(builder.getTokenType() == S_INCLUDE_KEYWORD)
				SharpIncludeKeyword.parse(builder);
			else if(builder.getTokenType() == S_DEFINE_KEYWORD)
				SharpDefineKeyword.parse(builder);
			else if(builder.getTokenType() == S_IFNDEF_KEYWORD || builder.getTokenType() == S_IFDEF_KEYWORD)
				SharpIfdefKeyword.parseIf(builder);
			//else if(builder.getTokenType() == TYPEDEF_KEYWORD)
			//	TypeDefParsing.parse(builder);
			else if(builder.getTokenType() == S_ENDIF_KEYWORD)
			{
				if(isSet(flags, EAT_LAST_END_IF))
					advanceLexerAndSkipLines(builder);
				else
					break;
			}
			else if(builder.getTokenType() == RBRACE)
				advanceLexerAndSkipLines(builder);
			else if(builder.getTokenType() == EXTERN_KEYWORD)
			{
				//TODO [VISTALL]
				advanceLexerAndSkipLines(builder);
			}
			else if(builder.getTokenType() == ENUM_KEYWORD)
				EnumParsing.parse(builder);
			else
				parseMethodOrField(builder);
		}
	}     */

	public static void parseTypeRef(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		if(builder != ELLIPSIS)
		{
			// *char
			// const char
			if(builder.getTokenType() == ASTERISK || builder.getTokenType() == CONST_KEYWORD || builder.getTokenType() == SIGNED_KEYWORD || builder.getTokenType() == UNSIGNED_KEYWORD)
				advanceLexerAndSkipLines(builder);

			advanceLexerAndSkipLines(builder);

			// char**
			while(builder.getTokenType() == ASTERISK)
				advanceLexerAndSkipLines(builder);
		}
		else
			builder.advanceLexer();

		done(marker, CPsiTypeRef.class);

		skipLines(builder);
	}


	public static void parseMethodOrField(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		parseTypeRef(builder);

		Class<? extends CPsiElement> clazz;

		if(builder.getTokenType() == CTokens.DOUBLE_COLON)
		{
			builder.advanceLexer();

			consumeIf(builder, CTokens.TILDE);
		}

		if(builder.getTokenType() != IDENTIFIER)
		{
			error(builder, "name.expected");
			done(marker, CPsiField.class);
			return;
		}
		else
			advanceLexerAndSkipLines(builder);

		if(builder.getTokenType() == LPARENTH)
		{
			ParameterListParsing.parseParameterList(builder);

			if(builder.getTokenType() != SEMICOLON)
			{
				CodeBlockParsing.parseCodeBlock(builder);

				done(marker, CPsiImplementingMethod.class);
			}
			else
			{
				builder.advanceLexer();

				done(marker, CPsiDeclarationMethod.class);

				skipLines(builder);
			}
		}
		else
		{
			if(builder.getTokenType() != SEMICOLON)
			{
				if(builder.getTokenType() != EQ)
					builder.error(CBundle.message("EQ.expected"));
				else
				{
					advanceLexerAndSkipLines(builder);

					ExpressionParsing.parseExpression(builder);

					if(builder.getTokenType() != SEMICOLON)
						builder.error(CBundle.message("SEMICOLON.expected"));

					builder.advanceLexer();
				}
			}
			else
				builder.advanceLexer();

			done(marker, CPsiField.class);

			skipLines(builder);
		}
	}
}
