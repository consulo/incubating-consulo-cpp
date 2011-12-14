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

		checkMatches(builder, LBRACE, "LBRACE.expected");

		int i = 0;
		while(!builder.eof())
		{
			skipLines(builder);

			PsiBuilder.Marker constantMarker = builder.mark();
			// name
			IElementType idenf = builder.getTokenType();

			if(idenf != IDENTIFIER)
			{
				builder.error(CBundle.message("name.expected"));

				advanceLexerAndSkipLines(builder);
			}
			else
			{
				advanceLexerAndSkipLines(builder);

				IElementType elementType = builder.getTokenType();
				if(elementType == EQ)
				{
					if(!LITERAL_EXPRESSION_SET.contains(lookAheadIgnoreLines(builder, 1)))
						builder.error(CBundle.message("index.expected") + " " + builder.getTokenType());
					else
					{
						advanceLexerAndSkipLines(builder);

						PsiBuilder.Marker marker1 = builder.mark();

						builder.advanceLexer();

						done(marker1, CPsiLiteralExpression.class);

						if(lookAheadIgnoreLines(builder, 1) != RBRACE)
							skipLines(builder);

						if(builder.getTokenType() == COMMA)
						{
							if(lookAheadIgnoreLines(builder, 1) != RBRACE)
								builder.advanceLexer();
							else
								builder.error(CBundle.message("COMMA.expected"));
						}
						else
						{
							
						}
					}
				}
				else
				{
					//if(elementType == COMMA)

					builder.advanceLexer();
				}
			}

			done(constantMarker, CPsiEnumConstant.class);

			i++;
			if(i == 4)
				break;
			if(builder.getTokenType() == RBRACE)
				break;
		}

		checkMatches(builder, RBRACE, "RBRACE.expected");

		checkMatches(builder, SEMICOLON, "SEMICOLON.expected");

		done(marker, CPsiEnum.class);

		skipLines(builder);
	}
}
