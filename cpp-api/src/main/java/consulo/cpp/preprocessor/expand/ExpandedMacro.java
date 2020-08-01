package consulo.cpp.preprocessor.expand;

import com.google.common.primitives.Ints;
import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author VISTALL
 * @since 21:52/2020-07-31
 */
public class ExpandedMacro {
	private List<Pair<IElementType, String>> mySymbols = new ArrayList<>();

	private ParserDefinition myParserDefinition;
	private CPreprocessorDefineDirective myElement;
	private CharSequence myText;

	private boolean myExpanded;

	private Set<Integer> myExpandedOffsets = new TreeSet<>();

	public ExpandedMacro(ParserDefinition parserDefinition, CPreprocessorDefineDirective element, CharSequence text) {
		myParserDefinition = parserDefinition;
		myElement = element;
		myText = text;
	}

	public CPreprocessorDefineDirective getElement() {
		return myElement;
	}

	public int[] getExpandedOffsets() {
		return Ints.toArray(myExpandedOffsets);
	}

	public void expand(int offset) {
		myExpandedOffsets.add(offset);

		if (myExpanded) {
			return;
		}

		myExpanded = true;

		Lexer lexer = myParserDefinition.createLexer(null);
		lexer.start(myText);

		IElementType type = null;
		while ((type = lexer.getTokenType()) != null) {
			mySymbols.add(Pair.create(type, lexer.getTokenText()));

			lexer.advance();
		}
	}

	public List<Pair<IElementType, String>> getSymbols() {
		return mySymbols;
	}
}
