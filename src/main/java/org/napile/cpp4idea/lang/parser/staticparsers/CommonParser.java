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
 *    limitations under the License.
 */

package org.napile.cpp4idea.lang.parser.staticparsers;

import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.parser.CElementType;
import org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword.SharpDefineKeyword;
import org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword.SharpIfdefKeyword;
import org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword.SharpIncludeKeyword;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 14:26/11.12.2011
 */
public class CommonParser implements CElementType, CTokenType
{
	public static void parseElement(PsiBuilder builder)
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
			else if(builder.getTokenType() == TYPEDEF_KEYWORD)
				TypeDefParser.parse(builder);
			else if(builder.getTokenType() == S_ENDIF_KEYWORD)
				advanceLexerAndSkipLines(builder);
			else
				parseMethodOrField(builder);
		}
	}

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

		marker.done(TYPE_REF_ELEMENT);

		skipLines(builder);
	}

	public static void parseCompilerVariable(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		builder.advanceLexer();

		marker.done(COMPILER_VARIABLE_ELEMENT);
	}

	public static void parseMethodOrField(PsiBuilder builder)
	{
		PsiBuilder.Marker maker = builder.mark();

		parseTypeRef(builder);

		IElementType methodName = builder.getTokenType();
		if(methodName != IDENTIFIER)
		{
			builder.error("Field name expected");
			maker.done(FIELD_ELEMENT);
			return;
		}
		else
			advanceLexerAndSkipLines(builder);

		if(builder.getTokenType() == LPARENTH)
		{
			ParameterListParser.parseParameterList(builder);
	
			if(builder.getTokenType() != SEMICOLON)
			{
				CodeBlockParser.parseCodeBlock(builder);
	
				maker.done(IMPLEMENTING_METHOD_ELEMENT);
			}
			else
			{
				builder.advanceLexer();
	
				maker.done(DECLARATION_METHOD_ELEMENT);

				skipLines(builder);
			}
		}
		else
		{
			if(builder.getTokenType() != SEMICOLON)
			{
				if(builder.getTokenType() != EQ)
					builder.error("';' expected");
				else
				{
					advanceLexerAndSkipLines(builder);

					CodeBlockParser.parseExpression(builder, maker);

					if(builder.getTokenType() != SEMICOLON)
						builder.error("';' expected");
				}
			}
			else
				builder.advanceLexer();

			maker.done(FIELD_ELEMENT);

			skipLines(builder);
		}
	}

	public static void advanceLexerAndSkipLines(PsiBuilder builder)
	{
		builder.advanceLexer();

		skipLines(builder);
	}

	public static void skipLines(PsiBuilder builder)
	{
		while(builder.getTokenType() == NEW_LINE)
			builder.advanceLexer();
	}
}
