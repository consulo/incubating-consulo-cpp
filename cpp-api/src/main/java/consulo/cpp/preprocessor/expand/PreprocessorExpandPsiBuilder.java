package consulo.cpp.preprocessor.expand;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import consulo.cpp.preprocessor.parser.InitialParserHelper;
import consulo.cpp.preprocessor.psi.CPreprocessorMacroReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

import java.util.List;

/**
 * @author VISTALL
 * @since 21:20/2020-07-31
 */
public class PreprocessorExpandPsiBuilder extends PsiBuilderAdapter {
	private class ExpandedMacro {
		public int position;

		public List<Pair<IElementType, String>> elements;
	}

	private PreprocessorExpander myExpander;

	private ExpandedMacro myExpandedMacro;

	public PreprocessorExpandPsiBuilder(@NotNull PsiBuilder delegate, PreprocessorExpander expander) {
		super(delegate);
		myExpander = expander;
	}

	@Override
	public @Nullable IElementType getTokenType() {
		lookForExpand();

		if (myExpandedMacro != null) {
			return myExpandedMacro.elements.get(myExpandedMacro.position).getFirst();
		}
		return super.getTokenType();
	}

	@Override
	public void advanceLexer() {
		lookForExpand();

		if (myExpandedMacro != null) {
			myExpandedMacro.position++;

			// macro finished
			if (myExpandedMacro.position == myExpandedMacro.elements.size()) {
				myExpandedMacro = null;

				Marker mark = super.mark();
				super.advanceLexer(); // macro reference advance
				InitialParserHelper.done(mark, CPreprocessorMacroReference.class);
			}
		} else {
			super.advanceLexer();
		}
	}

	private void lookForExpand() {
		IElementType tokenType = super.getTokenType();

		if (tokenType == CPsiTokens.IDENTIFIER) {
			List<Pair<IElementType, String>> list = myExpander.tryToExpand(super.getTokenText());

			if(list != null) {
				myExpandedMacro = new ExpandedMacro();
				myExpandedMacro.elements = list;
			}
		}
	}
}
