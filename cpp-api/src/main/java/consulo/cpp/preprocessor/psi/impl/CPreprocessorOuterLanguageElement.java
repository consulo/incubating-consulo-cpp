package consulo.cpp.preprocessor.psi.impl;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.language.ast.IElementType;
import consulo.language.impl.psi.template.OuterLanguageElementImpl;
import consulo.language.psi.OuterLanguageElement;

/**
 * @author VISTALL
 * @since 17:44/2020-07-31
 */
public class CPreprocessorOuterLanguageElement extends OuterLanguageElementImpl implements OuterLanguageElement
{
	public static final IElementType TYPE = new IElementType("OUTER", CPreprocessorLanguage.INSTANCE);

	public CPreprocessorOuterLanguageElement(CharSequence text)
	{
		super(TYPE, text);
	}
}
