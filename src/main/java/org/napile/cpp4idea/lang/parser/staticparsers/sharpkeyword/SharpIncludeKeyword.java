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

package org.napile.cpp4idea.lang.parser.staticparsers.sharpkeyword;

import org.napile.cpp4idea.lang.parser.staticparsers.CommonParsing;
import org.napile.cpp4idea.lang.psi.CPsiInclude;
import org.napile.cpp4idea.lang.psi.CPsiIndependInclude;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 7:19/11.12.2011
 */
public class SharpIncludeKeyword extends CommonParsing
{
	public static void parse(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		IElementType nextElement = builder.lookAhead(1);

		if(nextElement != STRING_LITERAL && nextElement != STRING_INCLUDE_LITERAL)
			builder.error("Incorrect include name");
		else
			builder.advanceLexer();

		builder.advanceLexer();

		done(marker, nextElement == STRING_LITERAL ? CPsiInclude.class : CPsiIndependInclude.class);

		skipLines(builder);
	}
}
