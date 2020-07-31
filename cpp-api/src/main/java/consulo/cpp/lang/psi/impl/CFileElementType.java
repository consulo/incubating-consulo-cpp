package consulo.cpp.lang.psi.impl;

import com.intellij.lang.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.tree.IStubFileElementType;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.expand.PreprocessorExpandPsiBuilder;
import consulo.cpp.preprocessor.expand.PreprocessorExpander;
import consulo.cpp.preprocessor.fileProvider.CFileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;

/**
 * @author VISTALL
 * @since 15:16/2020-07-31
 */
public class CFileElementType extends IStubFileElementType<PsiFileStub<PsiFile>> {
	public static final CFileElementType INSTANCE = new CFileElementType();

	public CFileElementType() {
		super(CLanguage.INSTANCE);
	}

	@Override
	protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {

		CFileViewProvider viewProvider = (CFileViewProvider) ((PsiFile) psi).getViewProvider();

		PsiFile preprocessorFile = viewProvider.getPsi(CPreprocessorLanguage.INSTANCE);

		ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.findSingle(CLanguage.INSTANCE);

		PreprocessorExpander expander = new PreprocessorExpander(preprocessorFile, parserDefinition);

		StringBuilder shortedText = expander.buildText(chameleon.getChars());

		Project project = psi.getProject();
		Language languageForParser = getLanguageForParser(psi);
		PsiBuilder original = PsiBuilderFactory.getInstance().createBuilder(parserDefinition, parserDefinition.createLexer(project), shortedText);
		PreprocessorExpandPsiBuilder builder = new PreprocessorExpandPsiBuilder(original, expander);

		PsiParser parser = LanguageParserDefinitions.INSTANCE.forLanguage(languageForParser).createParser(project);
		ASTNode node = parser.parse(this, builder);

		expander.insert(node.getFirstChildNode());

		return node.getFirstChildNode();
	}
}
