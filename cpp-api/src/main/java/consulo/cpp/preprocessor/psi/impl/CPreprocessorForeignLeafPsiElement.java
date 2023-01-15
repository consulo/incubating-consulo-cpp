package consulo.cpp.preprocessor.psi.impl;

import consulo.language.ast.IElementType;
import consulo.language.impl.ast.ForeignLeafType;
import consulo.language.impl.ast.TreeElement;
import consulo.language.impl.psi.ForeignLeafPsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 10:55/2020-08-01
 */
public class CPreprocessorForeignLeafPsiElement extends ForeignLeafPsiElement
{
	public CPreprocessorForeignLeafPsiElement(@NotNull IElementType type, CharSequence text)
	{
		super(new ForeignLeafType(type, text), text);
	}

	@Override
	public int getStartOffset()
	{
		int result = 0;
		TreeElement current = this;
		while(current.getTreeParent() != null)
		{
			result += current.getStartOffsetInParent();
			current = current.getTreeParent();
		}

		return result;
	}
}
