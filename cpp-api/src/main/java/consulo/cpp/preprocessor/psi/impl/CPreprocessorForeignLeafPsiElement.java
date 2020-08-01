package consulo.cpp.preprocessor.psi.impl;

import com.intellij.lang.ForeignLeafType;
import com.intellij.psi.impl.source.tree.ForeignLeafPsiElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 10:55/2020-08-01
 */
public class CPreprocessorForeignLeafPsiElement extends ForeignLeafPsiElement {
	public CPreprocessorForeignLeafPsiElement(@NotNull IElementType type, CharSequence text) {
		super(new ForeignLeafType(type, text), text);
	}

	@Override
	public int getStartOffset() {
		int result = 0;
		TreeElement current = this;
		while (current.getTreeParent() != null) {
			result += current.getStartOffsetInParent();
			current = current.getTreeParent();
		}

		return result;
	}
}
