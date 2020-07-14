package org.napile.cpp4idea.ide.highlight;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author VISTALL
 * @date 3:06/02.07.2012
 */
public class CSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
	@NotNull
	@Override
	public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
		if (project == null || virtualFile == null)
			return new PlainSyntaxHighlighter();
		else
			return new CSyntaxHighlighter(project, virtualFile);
	}
}
