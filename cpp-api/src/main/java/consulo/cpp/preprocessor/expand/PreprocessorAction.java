package consulo.cpp.preprocessor.expand;

/**
 * @author VISTALL
 * @since 17:26/2020-07-31
 */
public class PreprocessorAction {
	private CharSequence mySequence;

	public PreprocessorAction(CharSequence sequence) {
		mySequence = sequence;
	}

	public CharSequence getSequence() {
		return mySequence;
	}
}
