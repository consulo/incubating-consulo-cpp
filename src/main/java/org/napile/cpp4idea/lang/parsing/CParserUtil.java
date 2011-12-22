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

package org.napile.cpp4idea.lang.parsing;

import org.napile.cpp4idea.lang.psi.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 16:12/15.12.2011
 */
public class CParserUtil implements CTokenType
{
	public static void advanceLexerAndSkipLines(PsiBuilder builder)
	{
		builder.advanceLexer();

		skipLines(builder);
	}

	public static void skipLines(PsiBuilder builder)
	{
		while(builder.getTokenType() == NEW_LINE)
			builder.advanceLexer();
	}

	public static void parseCompilerVariable(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		builder.advanceLexer();

		done(marker, CPsiCompilerVariable.class);
	}

	public static void done(PsiBuilder.Marker marker, Class<? extends CPsiElement> clazz)
	{
		marker.done(CTokenElements.element(clazz));
	}
}
