package consulo.cpp.lang.editor.highlight;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.LanguageVersionableSyntaxHighlighterFactory;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.language.version.LanguageVersion;
import consulo.cpp.lang.CLanguage;
import consulo.cpp.lang.version.CLanguageVersion;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @date 3:06/02.07.2012
 */
@ExtensionImpl
public class CSyntaxHighlighterFactory extends LanguageVersionableSyntaxHighlighterFactory
{
	@Override
	public SyntaxHighlighter getSyntaxHighlighter(LanguageVersion languageVersion)
	{
		if(languageVersion instanceof CLanguageVersion cLanguageVersion)
		{
			return cLanguageVersion.getSyntaxHighlighter();
		}
		throw new IllegalArgumentException(languageVersion.toString());
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CLanguage.INSTANCE;
	}
}
