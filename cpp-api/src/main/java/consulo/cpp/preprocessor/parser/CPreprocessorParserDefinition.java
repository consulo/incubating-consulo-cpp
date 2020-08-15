package consulo.cpp.preprocessor.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.lexer._CPreprocessorLexer;
import consulo.cpp.preprocessor.psi.CPsiSharpTokenImpl;
import consulo.cpp.preprocessor.psi.impl.CPsiSharpFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 20:35/2020-07-25
 */
public class CPreprocessorParserDefinition implements ParserDefinition {
	public static final IFileElementType FILE_TYPE = new IFileElementType(CPreprocessorLanguage.INSTANCE);

	@Override
	public @NotNull Lexer createLexer(Project project) {
		return new FlexAdapter(new _CPreprocessorLexer());
	}

	@Override
	public PsiParser createParser(Project project) {
		return new CPreprocessorParser();
	}

	@Override
	public IFileElementType getFileNodeType() {
		return FILE_TYPE;
	}

	@Override
	public @NotNull TokenSet getWhitespaceTokens() {
		return TokenSet.create(TokenType.WHITE_SPACE);
		// TODO [VISTALL]  remove NEW_LINE?
		//return TokenSet.create(TokenType.WHITE_SPACE, CPsiTokens.NEW_LINE);
	}

	@Override
	public @NotNull TokenSet getCommentTokens() {
		return TokenSet.EMPTY;
	}

	@Override
	public @NotNull TokenSet getStringLiteralElements() {
		return TokenSet.EMPTY;
	}

	@Override
	public @NotNull PsiElement createElement(ASTNode node) {
		if (node.getElementType() instanceof CPsiSharpTokenImpl) {
			return ((CPsiSharpTokenImpl) node.getElementType()).createPsi(node);
		}

		throw new IllegalArgumentException("Illegal argument : " + node.getElementType());
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new CPsiSharpFileImpl(viewProvider);
	}
}
