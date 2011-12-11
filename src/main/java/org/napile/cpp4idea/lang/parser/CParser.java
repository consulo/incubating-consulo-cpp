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

package org.napile.cpp4idea.lang.parser;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.parser.staticparsers.CommonParser;
import org.napile.cpp4idea.lang.parser.staticparsers.ParameterListParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 2:19/10.12.2011
 */
public class CParser implements PsiParser, CTokenType, CElementType
{
	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder)
	{
		builder.setDebugMode(true);
		PsiBuilder.Marker rootMarker = builder.mark();

		CommonParser.parseElement(builder);

		rootMarker.done(root);

		return builder.getTreeBuilt();
	}

	@Deprecated
	public static void parseMethodOrExpression(PsiBuilder builder)
	{
		PsiBuilder.Marker maker = builder.mark();

		CommonParser.parseTypeRef(builder);

		IElementType methodName = builder.getTokenType();
		if(methodName != IDENTIFIER)
		{
			builder.error("Incorrect method name");
			maker.done(METHOD_ELEMENT);
			return;
		}
		else
			CommonParser.advanceLexerAndSkipLines(builder);

		ParameterListParser.parseParameterList(builder);

		if(builder.getTokenType() != SEMICOLON)
			parseCodeBlock(builder);
		else
			CommonParser.advanceLexerAndSkipLines(builder);

		maker.done(METHOD_ELEMENT);
	}

	private static void parseCodeBlock(PsiBuilder builder)
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

			parseExpression(builder);

			builder.advanceLexer();

			if(builder.getTokenType() != SEMICOLON)
				builder.error("; expected");

			marker.done(RETURN_ELEMENT);
		}

		builder.advanceLexer();
	}

	private static void parseExpression(PsiBuilder builder)
	{
		if(builder.getTokenType() == INTEGER_LITERAL)
		{

		}
	}
}
