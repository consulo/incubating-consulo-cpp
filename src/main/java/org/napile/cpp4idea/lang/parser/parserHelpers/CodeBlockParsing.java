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

package org.napile.cpp4idea.lang.parser.parserHelpers;

import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.lang.psi.CPsiCodeBlock;
import org.napile.cpp4idea.lang.psi.CPsiParameterList;
import org.napile.cpp4idea.lang.psi.CPsiReturnStatement;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 10:36/13.12.2011
 */
public class CodeBlockParsing extends CommonParsing
{
	public static void parseCodeBlock(PsiBuilder builder)
	{
		PsiBuilder.Marker marker;

		if(builder.getTokenType() != LBRACE)
		{
			builder.error(CBundle.message("LBRACE.expected"));
			marker = builder.mark();

			done(marker, CPsiParameterList.class);
			return;
		}
		else
		{
			marker = builder.mark();
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

		done(marker, CPsiCodeBlock.class);
	}

	private static void parseStatement(PsiBuilder builder)
	{
		if(builder.getTokenType() == RETURN_KEYWORD)
		{
			PsiBuilder.Marker marker = builder.mark();

			builder.advanceLexer();

			ExpressionParsing.parseExpression(builder);

			builder.advanceLexer();

			if(builder.getTokenType() != SEMICOLON)
				builder.error(CBundle.message("SEMICOLON.expected"));

			done(marker, CPsiReturnStatement.class);
		}

		builder.advanceLexer();
	}
}
