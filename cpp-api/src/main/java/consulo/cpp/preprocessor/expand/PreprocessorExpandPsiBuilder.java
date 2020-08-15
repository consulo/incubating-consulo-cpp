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
	private class ExpandedMacroPosition {
		public int position;

		public List<Pair<IElementType, String>> elements;
	}

	private final PreprocessorExpander myExpander;

	private ExpandedMacroPosition myExpandedMacroPosition;

	public PreprocessorExpandPsiBuilder(@NotNull PsiBuilder delegate, PreprocessorExpander expander) {
		super(delegate);
		myExpander = expander;
	}

	@Override
	public @Nullable IElementType getTokenType() {
		lookForExpand();

		if (myExpandedMacroPosition != null) {
			return myExpandedMacroPosition.elements.get(myExpandedMacroPosition.position).getFirst();
		}
		return super.getTokenType();
	}

	@Override
	public void advanceLexer() {
		lookForExpand();

		if (myExpandedMacroPosition != null) {
			myExpandedMacroPosition.position++;

			// macro finished
			if (myExpandedMacroPosition.position == myExpandedMacroPosition.elements.size()) {
				myExpandedMacroPosition = null;

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
			ExpandedMacro expandedMacro = myExpander.tryToExpand(super.getTokenText(), super.getCurrentOffset());

			if(expandedMacro != null) {
				myExpandedMacroPosition = new ExpandedMacroPosition();
				myExpandedMacroPosition.elements = expandedMacro.getSymbols();
			}
		}
	}
}
