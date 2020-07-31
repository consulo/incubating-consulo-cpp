package consulo.cpp.preprocessor.psi.impl;

import com.intellij.psi.templateLanguages.OuterLanguageElement;
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl;
import com.intellij.psi.tree.IElementType;
import consulo.cpp.preprocessor.CPreprocessorLanguage;

/**
 * @author VISTALL
 * @since 17:44/2020-07-31
 */
public class CPreprocessorOuterLanguageElement extends OuterLanguageElementImpl implements OuterLanguageElement {
	public static final IElementType TYPE = new IElementType("OUTER", CPreprocessorLanguage.INSTANCE);

	public CPreprocessorOuterLanguageElement(CharSequence text) {
		super(TYPE, text);
	}
}
