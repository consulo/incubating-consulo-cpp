package consulo.cpp.preprocessor.parser;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.lexer._CPreprocessorLexer;
import consulo.cpp.preprocessor.psi.CPsiSharpTokenImpl;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorFileImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.ast.TokenType;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.FlexAdapter;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 20:35/2020-07-25
 */
@ExtensionImpl
public class CPreprocessorParserDefinition implements ParserDefinition
{
	public static final IFileElementType FILE_TYPE = new IFileElementType(CPreprocessorLanguage.INSTANCE);

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CPreprocessorLanguage.INSTANCE;
	}

	@Nonnull
	@Override
	public Lexer createLexer(@Nonnull LanguageVersion languageVersion)
	{
		return new FlexAdapter(new _CPreprocessorLexer());
	}

	@Nonnull
	@Override
	public PsiParser createParser(@Nonnull LanguageVersion languageVersion)
	{
		return new CPreprocessorParser();
	}

	@Nonnull
	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE_TYPE;
	}

	@Nonnull
	@Override
	public TokenSet getWhitespaceTokens(@Nonnull LanguageVersion languageVersion)
	{
		return TokenSet.create(TokenType.WHITE_SPACE);
		// TODO [VISTALL]  remove NEW_LINE?
		//return TokenSet.create(TokenType.WHITE_SPACE, CPsiTokens.NEW_LINE);
	}

	@Nonnull
	@Override
	public TokenSet getCommentTokens(@Nonnull LanguageVersion languageVersion)
	{
		return TokenSet.EMPTY;
	}

	@Nonnull
	@Override
	public TokenSet getStringLiteralElements(@Nonnull LanguageVersion languageVersion)
	{
		return TokenSet.EMPTY;
	}

	@Nonnull
	@Override
	public PsiElement createElement(ASTNode node)
	{
		if(node.getElementType() instanceof CPsiSharpTokenImpl)
		{
			return ((CPsiSharpTokenImpl) node.getElementType()).createPsi(node);
		}

		throw new IllegalArgumentException("Illegal argument : " + node.getElementType());
	}

	@Nonnull
	@Override
	public PsiFile createFile(@Nonnull FileViewProvider viewProvider)
	{
		return new CPreprocessorFileImpl(viewProvider);
	}
}
