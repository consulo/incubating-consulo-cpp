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

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.psi.impl.CFileElementType;
import consulo.cpp.lang.psi.impl.CFileImpl;
import consulo.cpp.preprocessor.psi.CPsiSharpTokenImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.FlexAdapter;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.lang.lexer._CppLexer;
import org.napile.cpp4idea.lang.psi.CPsiTokenImpl;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @date 14:26/18.12.2011
 */
@ExtensionImpl
public class CParserDefinitionImpl implements ParserDefinition
{
	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CLanguage.INSTANCE;
	}

	@NotNull
	@Override
	public Lexer createLexer(LanguageVersion languageVersion)
	{
		return new FlexAdapter(new _CppLexer());
	}

	@Nonnull
	@Override
	public PsiParser createParser(LanguageVersion languageVersion)
	{
		return new CPsiParserImpl();
	}

	@Override
	public IFileElementType getFileNodeType()
	{
		return CFileElementType.INSTANCE;
	}

	@NotNull
	@Override
	public TokenSet getWhitespaceTokens(LanguageVersion languageVersion)
	{
		return CPsiTokens.WHITE_SPACE_SET;
	}

	@NotNull
	@Override
	public TokenSet getCommentTokens(LanguageVersion languageVersion)
	{
		return CPsiTokens.COMMENT_SET;
	}

	@NotNull
	@Override
	public TokenSet getStringLiteralElements(LanguageVersion languageVersion)
	{
		return CPsiTokens.STRING_LITERAL_SET;
	}

	@NotNull
	@Override
	public PsiElement createElement(ASTNode node)
	{
		if(node.getElementType() instanceof CPsiTokenImpl)
		{
			return ((CPsiTokenImpl) node.getElementType()).createPsi(node);
		}
		else if(node.getElementType() instanceof CPsiSharpTokenImpl)
		{
			return ((CPsiSharpTokenImpl) node.getElementType()).createPsi(node);
		}

		throw new IllegalArgumentException("Illegal argument : " + node.getElementType());
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new CFileImpl(viewProvider);
	}

	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return SpaceRequirements.MAY;
	}
}
