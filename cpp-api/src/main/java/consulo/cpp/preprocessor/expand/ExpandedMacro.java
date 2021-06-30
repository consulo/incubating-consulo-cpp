package consulo.cpp.preprocessor.expand;

import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.util.lang.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 21:52/2020-07-31
 */
public class ExpandedMacro
{
	private final List<Pair<IElementType, String>> mySymbols = new ArrayList<>();

	private final ParserDefinition myParserDefinition;
	private final CPreprocessorDefineDirective myElement;
	private final CharSequence myText;

	private boolean myExpanded;

	private final List<PreprocessorSymbolDoneInfo> mySymbolDoneInfos = new ArrayList<>();

	public ExpandedMacro(ParserDefinition parserDefinition, CPreprocessorDefineDirective element, CharSequence text)
	{
		myParserDefinition = parserDefinition;
		myElement = element;
		myText = text;
	}

	public CPreprocessorDefineDirective getElement()
	{
		return myElement;
	}

	public void addSymbolDoneInfo(@NotNull PreprocessorSymbolDoneInfo doneInfo)
	{
		mySymbolDoneInfos.add(doneInfo);
	}

	public List<PreprocessorSymbolDoneInfo> getSymbolDoneInfos()
	{
		return mySymbolDoneInfos;
	}

	public void expand()
	{
		if(myExpanded)
		{
			return;
		}

		myExpanded = true;

		if(!StringUtil.isEmpty(myText))
		{
			Lexer lexer = myParserDefinition.createLexer(null);
			lexer.start(myText);

			IElementType type;
			while((type = lexer.getTokenType()) != null)
			{
				mySymbols.add(Pair.create(type, lexer.getTokenText()));

				lexer.advance();
			}
		}

		if(mySymbols.isEmpty())
		{
			mySymbols.add(Pair.create(TokenType.WHITE_SPACE, ""));
		}
	}

	public List<Pair<IElementType, String>> getSymbols()
	{
		return mySymbols;
	}
}
