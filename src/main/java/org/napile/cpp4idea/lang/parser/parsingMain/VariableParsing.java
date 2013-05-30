/*
 * Copyright 2010-2013 napile.org
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

import org.napile.cpp4idea.lang.psi.CPsiElement;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 12:27/07.01.13
 */
public class VariableParsing extends MainParserHelper
{
	public static boolean parseVariable(Class<? extends CPsiElement> clazz, PsiBuilder builder)
	{
		PsiBuilder.Marker variableMarker = builder.mark();
		if(!MainParsing.parseTypeRef(builder))
		{
			variableMarker.drop();
			return false;
		}

		// Type *name

		consumeIf(builder, ASTERISK);

		if(!consumeIf(builder, IDENTIFIER))
		{
			error(builder, "IDENTIFIER.expected");
			done(variableMarker, clazz);
			return true;
		}

		while(consumeIf(builder, LBRACKET))
		{
			if(!consumeIf(builder, RBRACKET))
			{
				error(builder, "RBRACKET.expected");
				break;
			}
		}

		if(consumeIf(builder, EQ))
			ExpressionParsing.parsePrimaryExpression(builder);

		done(variableMarker, clazz);
		return true;
	}
}
