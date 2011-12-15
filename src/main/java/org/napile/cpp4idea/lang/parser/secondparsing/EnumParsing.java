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

package org.napile.cpp4idea.lang.parser.secondparsing;

import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.lang.psi.CPsiEnum;
import org.napile.cpp4idea.lang.psi.CPsiEnumConstant;
import org.napile.cpp4idea.lang.psi.CPsiLiteralExpression;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 18:01/14.12.2011
 */
public class EnumParsing extends CommonParsing
{
	public static void parse(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		advanceLexerAndSkipLines(builder);  // go to name

		if(builder.getTokenType() == IDENTIFIER)
			advanceLexerAndSkipLines(builder);

		if(builder.getTokenType() != LBRACE)
			builder.error(CBundle.message("LBRACE.expected"));

		while(!builder.eof())
		{
			advanceLexerAndSkipLines(builder);

			PsiBuilder.Marker constantMarker = builder.mark();
			// name
			IElementType idenf = builder.getTokenType();
			if(idenf != IDENTIFIER)
			{
				builder.advanceLexer();

				constantMarker.error(CBundle.message("constant.expected"));
			}
			else
			{
				IElementType nextElement = lookAheadIgnoreLines(builder, 1);

				PsiBuilder.Marker expressionMarker = null;
				if(nextElement == EQ)
				{
					// skip eq
					advanceLexerAndSkipLines(builder);

					if(!LITERAL_EXPRESSION_SET.contains(lookAheadIgnoreLines(builder, 1)))
					{
						builder.error(CBundle.message("expression.expected"));

						//advanceLexerAndSkipLines(builder);
					}
					else
					{
						advanceLexerAndSkipLines(builder);

						expressionMarker = builder.mark();
					}
				}

				nextElement = lookAheadIgnoreLines(builder, 1);
				if(nextElement == COMMA)
				{
					// goto comma
					advanceLexerAndSkipLines(builder);

					if(expressionMarker != null)
						done(expressionMarker, CPsiLiteralExpression.class);

					// skip comma
					builder.advanceLexer();
				}
				else if(nextElement == RBRACE)
				{
					// goto rbrace
					builder.advanceLexer();

					if(expressionMarker != null)
						done(expressionMarker, CPsiLiteralExpression.class);
				}
				else
				{
					if(lookAheadIgnoreLines(builder, 2) != RBRACE)
						builder.error(CBundle.message("COMMA.expected"));

					advanceLexerAndSkipLines(builder);

					if(expressionMarker != null)
						done(expressionMarker, CPsiLiteralExpression.class);
				}

				done(constantMarker, CPsiEnumConstant.class);
			}

			if(lookAheadIgnoreLines(builder, 1) == RBRACE)
				break;
		}

		advanceLexerAndSkipLines(builder); // goto RBRACE

		checkMatches(builder, RBRACE, "RBRACE.expected");

		checkMatches(builder, SEMICOLON, "SEMICOLON.expected");

		done(marker, CPsiEnum.class);

		skipLines(builder);
	}
}
