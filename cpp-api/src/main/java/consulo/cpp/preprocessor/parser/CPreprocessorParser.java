package consulo.cpp.preprocessor.parser;

import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;
import consulo.language.ast.ASTNode;
import consulo.language.parser.PsiParser;
import consulo.language.version.LanguageVersion;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 20:32/2020-07-25
 */
public class CPreprocessorParser implements PsiParser
{
	@Override
	public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder, LanguageVersion languageVersion)
	{
		builder.setDebugMode(true);

		return parseImpl(root, builder);
	}

	protected ASTNode parseImpl(@NotNull IElementType root, @NotNull PsiBuilder builder)
	{
		PsiBuilder.Marker rootMarker = builder.mark();

		CPreprocessorDirectiveParser.parse(builder, CPreprocessorDirectiveParser.EAT_LAST_END_IF);

		rootMarker.done(root);

		return builder.getTreeBuilt();
	}
}
