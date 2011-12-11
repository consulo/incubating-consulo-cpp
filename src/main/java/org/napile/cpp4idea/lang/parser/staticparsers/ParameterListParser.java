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
 * @date 14:38/11.12.2011
 */
public class ParameterListParser extends CommonParser
{
	public static void parseParameterList(PsiBuilder builder)
	{
		PsiBuilder.Marker maker;

		if(builder.getTokenType() != LPARENTH)
		{
			builder.error("( expected");
			maker = builder.mark();
			maker.done(PARAMETER_LIST_ELEMENT);
			return;
		}
		else
		{
			maker = builder.mark();
			advanceLexerAndSkipLines(builder);
		}

		if(builder.getTokenType() != RPARENTH)
		{
			parserParameter(builder);

			if(builder.getTokenType() != RPARENTH)
				builder.error(") expected");

			advanceLexerAndSkipLines(builder);
		}
		else
			advanceLexerAndSkipLines(builder);

		maker.done(PARAMETER_LIST_ELEMENT);
	}

	private static void parserParameter(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		final IElementType curToken = builder.getTokenType();

		// goto parameter name
		parseTypeRef(builder);

		if(builder.getTokenType() == IDENTIFIER)
			advanceLexerAndSkipLines(builder);

		marker.done(PARAMETER_ELEMENT);

		if(builder.getTokenType() == COMMA)
		{
			advanceLexerAndSkipLines(builder); // skip COMMA

			parserParameter(builder);
		}
	}
}
