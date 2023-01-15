package org.napile.cpp4idea.ide.highlight;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.SingleLazyInstanceSyntaxHighlighterFactory;
import consulo.language.editor.highlight.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @date 3:06/02.07.2012
 */
@ExtensionImpl
public class CSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory
{
	@Override
	protected @NotNull SyntaxHighlighter createHighlighter()
	{
		return new CSyntaxHighlighter();
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CLanguage.INSTANCE;
	}
}
