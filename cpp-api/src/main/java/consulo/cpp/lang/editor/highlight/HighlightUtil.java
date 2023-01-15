/*
 * Copyright 2010-2013 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.cpp.lang.editor.highlight;

import consulo.colorScheme.TextAttributesKey;
import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoHolder;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.language.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;
import org.napile.cpp4idea.lang.psi.CPsiEnumConstant;

/**
 * @author VISTALL
 * @date 11:30/02.01.13
 */
public class HighlightUtil
{
	public static void highlight(@NotNull PsiElement target, @NotNull PsiElement nameElement, @NotNull HighlightInfoHolder holder)
	{
		TextAttributesKey key = getKey(target);

		holder.add(HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION).textAttributes(key).range(nameElement).create());
	}

	private static TextAttributesKey getKey(PsiElement target)
	{
		if(target instanceof CPsiEnumConstant)
		{
			return CSyntaxHighlighter.CONSTANT;
		}
		else if(target instanceof CPreprocessorDefineDirective)
		{
			return CSyntaxHighlighter.COMPILER_VARIABLE;
		}

		throw new UnsupportedOperationException(target.getClass().getSimpleName());
	}
}
