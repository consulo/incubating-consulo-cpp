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
import org.napile.cpp4idea.lang.psi.CPsiTypeDeclaration;
import com.intellij.lang.PsiBuilder;

/**
 * @author VISTALL
 * @date 14:20/11.12.2011
 */
public class TypeDefParsing extends CommonParsing
{
	public static void parse(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		advanceLexerAndSkipLines(builder);

		parseTypeRef(builder);  // type

		parseTypeRef(builder);  // new type name

		if(builder.getTokenType() != SEMICOLON)
			builder.error(CBundle.message("SEMICOLON.expected"));
		else
			builder.advanceLexer();

		done(marker, CPsiTypeDeclaration.class);

		skipLines(builder);
	}
}
