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

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.config.facet.CFacetUtil;
import org.napile.cpp4idea.lang.CDialect;
import org.napile.cpp4idea.lang.psi.CTokens;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 14:28/18.12.2011
 */
public class CPsiParserImpl implements PsiParser, CTokens
{
	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder)
	{
		builder.setDebugMode(true);

		FacetCheck:
		{
			PsiFile psiFile = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
			if(psiFile != null)
			{
				CDialect dialect = CFacetUtil.findDialect(psiFile);

				if(dialect == null)
					break FacetCheck;

				final ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(CLanguage.INSTANCE);

				PsiBuilder newBuilder = PsiBuilderFactory.getInstance().createBuilder(parserDefinition, new FlexAdapter(dialect.getLexer()), builder.getOriginalText());

				return dialect.parseInitial(newBuilder, root);
			}
		}

		PsiBuilder.Marker rootMarker = builder.mark();
		while(!builder.eof())
			builder.advanceLexer();
		rootMarker.done(root);
		return builder.getTreeBuilt();
	}
}
