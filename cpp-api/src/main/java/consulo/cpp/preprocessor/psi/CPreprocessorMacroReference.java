package consulo.cpp.preprocessor.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.templateLanguages.OuterLanguageElement;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiElement;

/**
 * @author VISTALL
 * @since 21:45/2020-07-31
 */
public interface CPreprocessorMacroReference extends CPsiElement, OuterLanguageElement, PsiReference {
	@NotNull
	PsiElement getReferenceElement();
}
