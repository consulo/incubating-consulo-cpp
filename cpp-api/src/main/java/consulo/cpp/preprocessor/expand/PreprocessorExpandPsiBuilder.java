package consulo.cpp.preprocessor.expand;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import consulo.cpp.preprocessor.parser.CPreprocessorParserHelper;
import consulo.cpp.preprocessor.psi.CPreprocessorMacroReference;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author VISTALL
 * @since 21:20/2020-07-31
 */
public class PreprocessorExpandPsiBuilder extends PsiBuilderAdapter
{
	private static class ExpandedMacroPosition
	{
		public ExpandedMacro macro;

		public int offset;

		public int position;

		public List<Pair<IElementType, String>> elements;
	}

	private static class AdvanceToken
	{
		private ExpandedMacro myExpandedMacro;

		private int myTokenIndex;

		private int myTokenOffset;

		private AdvanceToken(ExpandedMacro expandedMacro, int tokenIndex, int tokenOffset)
		{
			myExpandedMacro = expandedMacro;
			myTokenIndex = tokenIndex;
			myTokenOffset = tokenOffset;
		}
	}

	protected class ExpandMarker extends DelegateMarker
	{
		private final List<AdvanceToken> myAdvancedTokens = new ArrayList<>();
		private final int myStartOffset;

		public ExpandMarker(@NotNull Marker delegate, int startOffset)
		{
			super(delegate);
			myStartOffset = startOffset;
		}

		public void advanceToken(AdvanceToken token)
		{
			myAdvancedTokens.add(token);
		}

		@Override
		public void drop()
		{
			super.drop();

			removeMarker(this, null);
		}

		@Override
		public void rollbackTo()
		{
			super.rollbackTo();
		}

		@Override
		public void done(@NotNull IElementType type)
		{
			super.done(type);

			removeMarker(this, type);
		}

		@Override
		public void doneBefore(@NotNull IElementType type, @NotNull Marker before)
		{
			super.doneBefore(type, before);

			removeMarker(this, type);
		}

		@Override
		public void doneBefore(@NotNull IElementType type, @NotNull Marker before, @NotNull LocalizeValue errorMessage)
		{
			super.doneBefore(type, before, errorMessage);

			removeMarker(this, type);
		}

		@Override
		public void collapse(@NotNull IElementType type)
		{
			super.collapse(type);

			removeMarker(this, type);
		}
	}

	private final PreprocessorExpander myExpander;

	private ExpandedMacroPosition myExpandedMacroPosition;

	private final Deque<ExpandMarker> myMarkers = new ArrayDeque<>();

	public PreprocessorExpandPsiBuilder(@NotNull PsiBuilder delegate, PreprocessorExpander expander)
	{
		super(delegate);
		myExpander = expander;
	}

	@Override
	public
	@Nullable
	IElementType getTokenType()
	{
		ExpandedMacroPosition position = lookForExpand();

		if(position != null)
		{
			return position.elements.get(position.position).getFirst();
		}
		return super.getTokenType();
	}

	@Override
	public void advanceLexer()
	{
		ExpandedMacroPosition position = lookForExpand();

		if(position != null)
		{
			int tokenPosition = position.position++;

			ExpandMarker last = myMarkers.peekLast();
			if(last != null)
			{
				last.advanceToken(new AdvanceToken(position.macro, tokenPosition, position.offset));
			}

			// macro finished
			if(position.position == position.elements.size())
			{
				myExpandedMacroPosition = null;

				Marker mark = super.mark();
				super.advanceLexer(); // macro reference advance
				CPreprocessorParserHelper.done(mark, CPreprocessorMacroReference.class);
			}
		}
		else
		{
			super.advanceLexer();
		}
	}

	@Override
	public
	@NotNull
	Marker mark()
	{
		ExpandMarker marker = new ExpandMarker(super.mark(), super.getCurrentOffset());
		myMarkers.addLast(marker);
		return marker;
	}

	private void removeMarker(ExpandMarker marker, @Nullable IElementType doneElementType)
	{
		myMarkers.remove(marker);

		if(marker.myAdvancedTokens.isEmpty())
		{
			return;
		}

		if(doneElementType != null)
		{
			List<AdvanceToken> doneInfos = marker.myAdvancedTokens;
			for(AdvanceToken advancedToken : doneInfos)
			{
				ExpandedMacro macro = advancedToken.myExpandedMacro;

				macro.addSymbolDoneInfo(new PreprocessorSymbolDoneInfo(advancedToken.myTokenOffset, advancedToken.myTokenIndex, new AstElementTypeId(marker.myStartOffset, doneElementType)));
			}
		}
	}

	@Nullable
	private ExpandedMacroPosition lookForExpand()
	{
		ExpandedMacroPosition currentPosition = myExpandedMacroPosition;
		if(currentPosition != null)
		{
			return currentPosition;
		}

		IElementType tokenType = super.getTokenType();

		if(tokenType == CPsiTokens.IDENTIFIER)
		{
			ExpandedMacro expandedMacro = myExpander.tryToExpand(super.getTokenText());

			if(expandedMacro != null)
			{
				ExpandedMacroPosition position = new ExpandedMacroPosition();
				position.macro = expandedMacro;
				position.offset = super.getCurrentOffset();

				myExpandedMacroPosition = position;
				myExpandedMacroPosition.elements = expandedMacro.getSymbols();
				return position;
			}
		}

		return null;
	}
}
