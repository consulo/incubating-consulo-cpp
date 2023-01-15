package consulo.cpp.preprocessor.expand;

/**
 * @author VISTALL
 * @since 17:26/2020-08-15
 */
public class PreprocessorSymbolDoneInfo
{
	private final int myTokenOffset;
	private final int myTokenIndex;
	private final AstElementTypeId myDoneElementType;

	public PreprocessorSymbolDoneInfo(int tokenOffset, int tokenIndex, AstElementTypeId doneElementType)
	{
		myTokenOffset = tokenOffset;
		myTokenIndex = tokenIndex;
		myDoneElementType = doneElementType;
	}

	public int getTokenOffset()
	{
		return myTokenOffset;
	}

	public int getTokenIndex()
	{
		return myTokenIndex;
	}

	public AstElementTypeId getDoneElementType()
	{
		return myDoneElementType;
	}
}
