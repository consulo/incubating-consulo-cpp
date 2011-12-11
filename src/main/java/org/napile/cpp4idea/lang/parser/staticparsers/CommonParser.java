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
import org.napile.cpp4idea.lang.parser.CParser;
import org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword.SharpDefineKeyword;
import org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword.SharpIfdefKeyword;
import org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword.SharpIncludeKeyword;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 14:26/11.12.2011
 */
public class CommonParser implements CElementType, CTokenType
{
	public static void parseElement(PsiBuilder builder)
	{
		skipLines(builder);

		while(!builder.eof())
		{
			if(builder.getTokenType() == S_INCLUDE_KEYWORD)
				SharpIncludeKeyword.parse(builder);
			else if(builder.getTokenType() == S_DEFINE_KEYWORD)
				SharpDefineKeyword.parse(builder);
			else if(builder.getTokenType() == S_IFNDEF_KEYWORD)
				SharpIfdefKeyword.parse(builder);
			else if(builder.getTokenType() == TYPEDEF_KEYWORD)
				TypeDefParser.parse(builder);
			else if(builder.getTokenType() == S_ENDIF_KEYWORD)
				advanceLexerAndSkipLines(builder);
			else
				CParser.parseMethodOrExpression(builder);
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
