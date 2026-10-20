package consulo.cpp.preprocessor.parser;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.CPreprocessorFileElementType;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorFileImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementTypeAsPsiFactory;
import consulo.language.ast.IFileElementType;
import consulo.language.file.FileViewProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersionableParserDefinition;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 20:35/2020-07-25
 */
@ExtensionImpl
public class CPreprocessorParserDefinition extends LanguageVersionableParserDefinition {
    public static final IFileElementType FILE_TYPE = CPreprocessorFileElementType.INSTANCE;

    @Nonnull
    @Override
    public Language getLanguage() {
        return CPreprocessorLanguage.INSTANCE;
    }

    @Nonnull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE_TYPE;
    }

    @Nonnull
    @Override
    public PsiElement createElement(ASTNode node) {
        // element types build their own PSI, which is what lets a dialect plugin add nodes of its own
        if (node.getElementType() instanceof IElementTypeAsPsiFactory factory) {
            return factory.createElement(node);
        }

        throw new IllegalArgumentException("Illegal argument : " + node.getElementType());
    }

    @Nonnull
    @Override
    public PsiFile createFile(@Nonnull FileViewProvider viewProvider) {
        return new CPreprocessorFileImpl(viewProvider);
    }
}
