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
import org.napile.cpp4idea.lang.parser.CElementType;
import org.napile.cpp4idea.lang.parser.staticparsers.CommonParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 14:05/11.12.2011
 */
public class SharpIfdefKeyword extends CommonParser implements CElementType, CTokenType
{
	public static void parseIf(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		IElementType elementType = builder.getTokenType();

		advanceLexerAndSkipLines(builder);

		parseCompilerVariable(builder);

		while(!builder.eof())
		{
			if(builder.getTokenType() == S_ENDIF_KEYWORD)
				break;

			CommonParser.parseElement(builder);
		}

		advanceLexerAndSkipLines(builder);

		marker.done(elementType == S_IFDEF_KEYWORD ? IF_DEF_ELEMENT : IF_NOT_DEF_ELEMENT);
	}
}
