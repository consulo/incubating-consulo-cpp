package consulo.cpp.preprocessor;

import com.intellij.lang.Language;

/**
 * @author VISTALL
 * @since 19:27/2020-07-15
 */
public class CPreprocessorLanguage extends Language {
	public static final CPreprocessorLanguage INSTANCE = new CPreprocessorLanguage();

	private CPreprocessorLanguage() {
		super("CPreprocessor");
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}
}
