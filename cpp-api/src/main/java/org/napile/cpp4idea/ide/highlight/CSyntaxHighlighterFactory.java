package org.napile.cpp4idea.ide.highlight;

import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @date 3:06/02.07.2012
 */
public class CSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
	@Override
	protected @NotNull SyntaxHighlighter createHighlighter() {
		return new CSyntaxHighlighter();
	}
}
