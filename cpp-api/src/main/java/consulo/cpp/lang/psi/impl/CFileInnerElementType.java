package consulo.cpp.lang.psi.impl;

import consulo.cpp.preprocessor.expand.PreprocessorExpandPsiBuilder;
import consulo.cpp.preprocessor.expand.PreprocessorExpander;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.file.FileViewProvider;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderFactory;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionUtil;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;

/**
 * @author VISTALL
 * @since 15:27/2020-08-15
 * <p>
 * File Element for C/C++ file but without preprocessor element removing
 */
public class CFileInnerElementType extends IFileElementType
{
	public static final CFileInnerElementType INSTANCE = new CFileInnerElementType();

	public CFileInnerElementType()
	{
		super(CLanguage.INSTANCE);
	}

	@Override
	protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi)
	{
		FileViewProvider viewProvider = psi.getContainingFile().getViewProvider();

		PreprocessorExpander expander = viewProvider.getUserData(PreprocessorExpander.EXPANDER_KEY);

		Project project = psi.getProject();
		Language languageForParser = getLanguageForParser(psi);
		LanguageVersion languageVersion = LanguageVersionUtil.findDefaultVersion(languageForParser);

		PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, null, languageForParser, languageVersion, chameleon.getChars());
		if(expander != null)
		{
			builder = new PreprocessorExpandPsiBuilder(builder, expander);
		}

		PsiParser parser = ParserDefinition.forLanguage(languageForParser).createParser(languageVersion);
		ASTNode node = parser.parse(this, builder, languageVersion);
		return node.getFirstChildNode();
	}
}
