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

import consulo.language.parser.PsiBuilder;
import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.lang.psi.CPsiParameter;
import org.napile.cpp4idea.lang.psi.CPsiParameterList;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 14:38/11.12.2011
 */
public class ParameterListParsing extends MainParsing
{
	public static void parseParameterList(PsiBuilder builder)
	{
		PsiBuilder.Marker marker;

		if(builder.getTokenType() != LPARENTH)
		{
			builder.error(CBundle.message("LPARENTH.expected"));
			marker = builder.mark();
			builder.advanceLexer();
			done(marker, CPsiParameterList.class);
			return;
		}
		else
		{
			marker = builder.mark();
			advanceLexerAndSkipLines(builder);
		}

		if(builder.getTokenType() != RPARENTH)
		{
			while(true)
			{
				if(builder.getTokenType() == ELLIPSIS)
				{
					doneOneToken(builder, CPsiParameter.class);
				}
				else
				{
					VariableParsing.parseVariable(CPsiParameter.class, builder);
				}

				if(!consumeIf(builder, COMMA))
				{
					break;
				}
			}

			expect(builder, RPARENTH, "RPARENTH.expected");
		}
		else
		{
			advanceLexerAndSkipLines(builder);
		}

		done(marker, CPsiParameterList.class);
	}

	private static void parserParameter(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		// goto parameter name
		parseTypeRef(builder);

		if(builder.getTokenType() == IDENTIFIER)
		{
			advanceLexerAndSkipLines(builder);
		}

		if(builder.getTokenType() == CPsiTokens.LBRACKET)
		{
			advanceLexerAndSkipLines(builder);
			expect(builder, CPsiTokens.RBRACKET, "RBRACKET.expected");
		}

		done(marker, CPsiParameter.class);

		while(builder.getTokenType() == COMMA)
		{
			advanceLexerAndSkipLines(builder); // skip COMMA

			parserParameter(builder);
		}
	}
}
