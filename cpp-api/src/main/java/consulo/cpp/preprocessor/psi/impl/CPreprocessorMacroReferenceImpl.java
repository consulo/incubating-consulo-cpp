package consulo.cpp.preprocessor.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import consulo.cpp.preprocessor.psi.CPreprocessorMacroReference;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 21:44/2020-07-31
 */
public class CPreprocessorMacroReferenceImpl extends ASTWrapperPsiElement implements CPreprocessorMacroReference {
	public CPreprocessorMacroReferenceImpl(@NotNull ASTNode node) {
		super(node);
	}
}
