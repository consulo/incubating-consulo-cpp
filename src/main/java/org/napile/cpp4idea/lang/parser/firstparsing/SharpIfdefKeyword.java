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

package org.napile.cpp4idea.lang.parser.firstparsing;

import org.napile.cpp4idea.lang.parser.CParserUtil;
import org.napile.cpp4idea.lang.parser.secondparsing.CommonParsing;
import org.napile.cpp4idea.lang.psi.CPsiIfDefHolder;
import org.napile.cpp4idea.lang.psi.CPsiIfNotDefHolder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 14:05/11.12.2011
 */
public class SharpIfdefKeyword extends CommonParsing
{
	public static void parseIf(PsiBuilder builder)
	{
		IElementType elementType = builder.getTokenType();

		PsiBuilder.Marker marker = builder.mark();

		CParserUtil.advanceLexerAndSkipLines(builder);
		if(builder.getTokenType() == IDENTIFIER)
			CParserUtil.parseCompilerVariable(builder);

		while(!builder.eof())
		{
			builder.advanceLexer();
			if(builder.getTokenType() == S_ENDIF_KEYWORD)
				break;
		}

		if(builder.getTokenType() != S_ENDIF_KEYWORD)
			builder.error("#endif expected");
		else
			builder.advanceLexer();

		CParserUtil.done(marker, elementType == S_IFDEF_KEYWORD ? CPsiIfDefHolder.class : CPsiIfNotDefHolder.class);
	}
}
