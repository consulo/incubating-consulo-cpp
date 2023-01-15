package consulo.cpp.preprocessor.expand;

import consulo.document.util.TextRange;
import consulo.language.impl.psi.template.TemplateDataModifications;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 14:57/2020-08-15
 * <p>
 * Initial code from {@link TemplateDataModifications}
 */
public class CTemplateDataModifications
{
	public static final CTemplateDataModifications EMPTY = new CTemplateDataModifications(Collections.emptyList());

	final @NotNull List<TextRange> myOuterAndRemoveRanges;

	public CTemplateDataModifications()
	{
		this(new ArrayList<>());
	}

	private CTemplateDataModifications(@NotNull List<TextRange> ranges)
	{
		myOuterAndRemoveRanges = ranges;
	}

	/**
	 * @see TemplateDataElementType.RangeCollector#addOuterRange(TextRange)
	 */
	public void addOuterRange(@NotNull TextRange newRange)
	{
		addOuterRange(newRange, false);
	}

	/**
	 * @see TemplateDataElementType.RangeCollector#addOuterRange(TextRange, boolean)
	 */
	public void addOuterRange(@NotNull TextRange range, boolean isInsertion)
	{
		myOuterAndRemoveRanges.add(isInsertion ? new CPreprocessorDirectiveCollector.InsertionRange(range.getStartOffset(), range.getEndOffset()) : range);
	}

	/**
	 * @see TemplateDataElementType.RangeCollector#addRangeToRemove(TextRange)
	 */
	public void addRangeToRemove(int startOffset, @NotNull CharSequence textToInsert)
	{
		myOuterAndRemoveRanges.add(new CPreprocessorDirectiveCollector.RangeToRemove(startOffset, textToInsert));
	}

	public static @NotNull TemplateDataModifications fromRangeToRemove(int startOffset, @NotNull CharSequence textToInsert)
	{
		TemplateDataModifications modifications = new TemplateDataModifications();
		modifications.addRangeToRemove(startOffset, textToInsert);
		return modifications;
	}

	public boolean addAll(@NotNull CTemplateDataModifications other)
	{
		return myOuterAndRemoveRanges.addAll(other.myOuterAndRemoveRanges);
	}
}
