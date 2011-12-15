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
import org.napile.cpp4idea.lang.psi.CPsiParameter;
import org.napile.cpp4idea.lang.psi.CPsiParameterList;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 14:38/11.12.2011
 */
public class ParameterListParsing extends CommonParsing
{
	public static void parseParameterList(PsiBuilder builder)
	{
		PsiBuilder.Marker marker;

		if(builder.getTokenType() != LPARENTH)
		{
			builder.error(CBundle.message("LPARENTH.expected"));
			marker = builder.mark();
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
			parserParameter(builder);

			if(builder.getTokenType() != RPARENTH)
				builder.error(CBundle.message("RPARENTH.expected"));

			advanceLexerAndSkipLines(builder);
		}
		else
			advanceLexerAndSkipLines(builder);

		done(marker, CPsiParameterList.class);

		//skipLines(builder);
	}

	private static void parserParameter(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		// goto parameter name
		parseTypeRef(builder);

		if(builder.getTokenType() == IDENTIFIER)
			advanceLexerAndSkipLines(builder);

		done(marker, CPsiParameter.class);

		if(builder.getTokenType() == COMMA)
		{
			advanceLexerAndSkipLines(builder); // skip COMMA

			parserParameter(builder);
		}
	}
}
