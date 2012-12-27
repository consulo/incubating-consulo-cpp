package org.napile.cpp4idea.editor;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.util.CPsiUtil;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 3:38/02.07.2012
 */
public class CCompilerVariablesAnnotator implements Annotator
{
	private static final CSyntaxHighlighter HIGHLIGHTER = new CSyntaxHighlighter();

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
	{
		boolean isDefined = CPsiUtil.isBlockDefined(element, new HashSet<String>());
		if(!isDefined)
		{
			TextAttributesKey[] textAttributesKeys = HIGHLIGHTER.getAttributes(element.getNode().getElementType());
			if(textAttributesKeys != null)
			{
				Annotation annotation = holder.createInfoAnnotation(element, null);

				annotation.setTextAttributes(textAttributesKeys[1]);
			}
		}
	}
}
