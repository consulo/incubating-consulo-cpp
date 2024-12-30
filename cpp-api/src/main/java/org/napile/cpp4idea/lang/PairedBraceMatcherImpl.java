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

package org.napile.cpp4idea.lang;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.BracePair;
import consulo.language.Language;
import consulo.language.PairedBraceMatcher;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @date 5:52/11.12.2011
 */
@ExtensionImpl
public class PairedBraceMatcherImpl implements PairedBraceMatcher
{
	private static final BracePair[] PAIRS =
			{
					new BracePair(CPsiTokens.LPARENTH, CPsiTokens.RPARENTH, true),
					new BracePair(CPsiTokens.LBRACE, CPsiTokens.RBRACE, false),
					new BracePair(CPsiTokens.S_IFDEF_KEYWORD, CPsiTokens.S_ENDIF_KEYWORD, false),
					new BracePair(CPsiTokens.S_IFNDEF_KEYWORD, CPsiTokens.S_ENDIF_KEYWORD, false)
			};

	@Override
	public BracePair[] getPairs()
	{
		return PAIRS;
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CLanguage.INSTANCE;
	}
}
