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

package org.napile.cpp4idea.util;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 18:20/07.01.13
 */
public class CPsiUtil
{
	public static PsiElement getElementByTextRange(@NotNull PsiElement element, @NotNull TextRange textRange)
	{
		if(element.getTextRange().equals(textRange))
			return element;

		if(element.getNode().getElementType() == CPsiTokens.IDENTIFIER)
		{
			System.out.println(element.getText() + " " + element.getTextRange() + " " + textRange);
		}
		PsiElement el = element.getFirstChild();
		while(el != null)
		{
			PsiElement temp = getElementByTextRange(el, textRange);
			if(temp != null)
				return temp;

			el = el.getNextSibling();
		}
		return null;
	}
}
