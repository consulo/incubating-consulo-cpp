package org.napile.cpp4idea;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;

/**
 * @author VISTALL
 * @date 0:57/10.12.2011
 */
public class CLanguage extends Language
{
	public static final Language INSTANCE = new CLanguage();

	public CLanguage()
	{
		super("C");
		SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory()
		{
			@NotNull
			protected SyntaxHighlighter createHighlighter()
			{
				return new CSyntaxHighlighter();
			}
		});
	}

	@Override
	public String getDisplayName()
	{
		return "C/C++";
	}

	@Override
	public boolean isCaseSensitive()
	{
		return true;
	}
}
