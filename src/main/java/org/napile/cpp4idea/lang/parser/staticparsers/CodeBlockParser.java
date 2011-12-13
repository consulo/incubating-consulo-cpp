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

import org.napile.cpp4idea.CBundle;
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
			builder.error(CBundle.message("LBRACE.expected"));
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
				builder.error(CBundle.message("RBRACE.expected"));
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

			parseExpressionBlock(builder, builder.getTokenType(), SEMICOLON);

			builder.advanceLexer();

			if(builder.getTokenType() != SEMICOLON)
				builder.error(CBundle.message("SEMICOLON.expected"));

			marker.done(RETURN_ELEMENT);
		}

		builder.advanceLexer();
	}

	public static boolean parseExpressionBlock(PsiBuilder builder, IElementType elementType, IElementType needEndElement)
	{
		if(LITERAL_EXPRESSION_SET.contains(builder.getTokenType()))
		{
			PsiBuilder.Marker marker = builder.mark();

			doneAndSkipLines(LITERAL_EXPRESSION_ELEMENT, builder);

			if(BINARY_EXPRESSION_SET.contains(builder.getTokenType()))
			{
				IElementType prev = builder.getTokenType();

				advanceLexerAndSkipLines(builder);

				if(!parseExpressionBlock(builder, prev, needEndElement))
					builder.error(CBundle.message("expression.expected"));

				marker.done(BINARY_EXPRESSION_ELEMENT);

				return true;
			}
			/*else if(builder.getTokenType() == LPARENTH)
			{
				IElementType prev = builder.getTokenType();

				advanceLexerAndSkipLines(builder);

				if(!parseExpressionBlock(builder, prev))
					builder.error(CBundle.message("expression.expected") + " " + builder.getTokenType());

				marker.done(PARENTHESIZED_EXPRESSION_ELEMENT);

				return true;
			} */
			else
			{
				marker.drop();

				return needEndElement == null || builder.getTokenType() == needEndElement;
			}
		}
		else if(BINARY_EXPRESSION_SET.contains(builder.getTokenType()))
		{
			IElementType prev = builder.getTokenType();

			advanceLexerAndSkipLines(builder);

			return parseExpressionBlock(builder, prev, SEMICOLON);
		}
		else if(builder.getTokenType() == LPARENTH)
		{
			IElementType prev = builder.getTokenType();

			PsiBuilder.Marker marker = builder.mark();

			advanceLexerAndSkipLines(builder);

			if(!parseExpressionBlock(builder, prev, RPARENTH))
				builder.error(CBundle.message("expression.expected"));

			if(builder.getTokenType() != RPARENTH)
				builder.error(CBundle.message("RPARENTH.expected"));

			builder.advanceLexer();

			marker.done(PARENTHESIZED_EXPRESSION_ELEMENT);

			skipLines(builder);

			parseExpressionBlock(builder, prev, SEMICOLON);
			return true;
		}
		else if(builder.getTokenType() == SEMICOLON)
		{
			if(elementType == null || !BINARY_EXPRESSION_SET.contains(elementType))
				return true;
		}

		builder.error(CBundle.message("expression.expected"));
		return false;
	}
}
