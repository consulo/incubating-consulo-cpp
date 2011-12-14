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

package org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword;

import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.parser.staticparsers.CommonParsing;
import org.napile.cpp4idea.lang.psi.CPsiDefine;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 7:21/11.12.2011
 */
public class SharpDefineKeyword extends CommonParsing implements CTokenType
{
	public static void parse(PsiBuilder builder)
	{
		PsiBuilder.Marker maker = builder.mark();

		// #define
		advanceLexerAndSkipLines(builder);

		// var name
		parseCompilerVariable(builder);

		while(!builder.eof())
		{
			if(builder.getTokenType() == NEW_LINE)
				break;

			if(builder.getTokenType() == NEXT_LINE)
				builder.advanceLexer();

			builder.advanceLexer();
		}

		done(maker, CPsiDefine.class);

		skipLines(builder);
	}
}
