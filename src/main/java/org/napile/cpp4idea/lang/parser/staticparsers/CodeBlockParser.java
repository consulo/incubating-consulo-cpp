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

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 10:36/13.12.2011
 */
public class CodeBlockParser extends CommonParser
{
	public static void parseCodeBlock(PsiBuilder builder)
	{
		PsiBuilder.Marker maker;

		if(builder.getTokenType() != LBRACE)
		{
			builder.error("{ expected");
			maker = builder.mark();
			maker.done(PARAMETER_LIST_ELEMENT);
			return;
		}
		else
		{
			maker = builder.mark();
			builder.advanceLexer();
		}

		if(builder.getTokenType() != RBRACE)
		{
			while(!builder.eof())
			{
				parseStatement(builder);

				if(builder.getTokenType() == RBRACE)
					break;
			}

			if(builder.getTokenType() != RBRACE)
				builder.error("} expected");
		}

		builder.advanceLexer();

		maker.done(CODE_BLOCK_ELEMENT);
	}

	private static void parseStatement(PsiBuilder builder)
	{
		if(builder.getTokenType() == RETURN_KEYWORD)
		{
			PsiBuilder.Marker marker = builder.mark();

			builder.advanceLexer();

			parseExpression(builder, marker);

			builder.advanceLexer();

			if(builder.getTokenType() != SEMICOLON)
				builder.error("; expected");

			marker.done(RETURN_ELEMENT);
		}

		builder.advanceLexer();
	}

	public static void parseExpressionBlock(PsiBuilder builder)
	{

	}

	public static void parseExpression(PsiBuilder builder, PsiBuilder.Marker rootMaker)
	{
		IElementType last = null;

		PsiBuilder.Marker marker = builder.mark();
		while(!builder.eof())
		{
			if(builder.getTokenType() == INTEGER_LITERAL)
				last = LiteralParser.parseIntegerLiteral(builder);
			else if(builder.getTokenType() == PLUS)
			{
				System.out.println(builder.lookAhead(-2));

				advanceLexerAndSkipLines(builder);
			}
			else if(builder.getTokenType() == LPARENTH)
			{
				throw new IllegalArgumentException();
			}
			else if(builder.getTokenType() == SEMICOLON)
			{
				if(last == null)
					marker.error("Expression expected");
				else
					marker.drop();

				builder.advanceLexer();
				return;
			}
			else
				break;
		}

		builder.error(last == null ? "Expression expected" : "; expected");

		marker.drop();
		advanceLexerAndSkipLines(builder);
	}
}
