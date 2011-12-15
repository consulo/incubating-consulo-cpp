/*
 * Copyright 2011 napile
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
