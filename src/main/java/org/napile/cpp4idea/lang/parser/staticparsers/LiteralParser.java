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

import org.jetbrains.annotations.Nullable;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 10:28/13.12.2011
 */
public class LiteralParser extends CommonParser
{
	@Nullable
	public static IElementType parseIntegerLiteral(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		builder.advanceLexer();

		marker.done(LITERAL_EXPRESSION_ELEMENT);

		skipLines(builder);

		return LITERAL_EXPRESSION_ELEMENT;
	}
}
