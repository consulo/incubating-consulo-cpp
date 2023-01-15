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

package org.napile.cpp4idea.lang.parser;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiParser;
import consulo.language.version.LanguageVersion;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.parser.parsingMain.MainParsing;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 14:28/18.12.2011
 */
public class CPsiParserImpl implements PsiParser, CPsiTokens
{
	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder, LanguageVersion languageVersion)
	{
		builder.setDebugMode(true);

		return parseMain(builder, root);
	}

	public static ASTNode parseMain(PsiBuilder builder, IElementType root)
	{
		PsiBuilder.Marker rootMarker = builder.mark();

		MainParsing.parseElement(builder);

		rootMarker.done(root);

		return builder.getTreeBuilt();
	}
}
