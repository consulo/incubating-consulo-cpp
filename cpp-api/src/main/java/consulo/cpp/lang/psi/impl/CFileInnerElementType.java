package consulo.cpp.lang.psi.impl;

import com.intellij.lang.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IFileElementType;
import consulo.cpp.preprocessor.expand.PreprocessorExpandPsiBuilder;
import consulo.cpp.preprocessor.expand.PreprocessorExpander;
import consulo.lang.LanguageVersion;
import consulo.lang.util.LanguageVersionUtil;
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

		PsiParser parser = LanguageParserDefinitions.INSTANCE.forLanguage(languageForParser).createParser(languageVersion);
		ASTNode node = parser.parse(this, builder, languageVersion);
		return node.getFirstChildNode();
	}
}
