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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 5:52/11.12.2011
 */
public class PairedBraceMatcherImpl implements PairedBraceMatcher {
	private static final BracePair[] PAIRS =
			{
					new BracePair(CPsiTokens.LPARENTH, CPsiTokens.RPARENTH, true),
					new BracePair(CPsiTokens.LBRACE, CPsiTokens.RBRACE, false),
					new BracePair(CPsiTokens.S_IFDEF_KEYWORD, CPsiTokens.S_ENDIF_KEYWORD, false),
					new BracePair(CPsiTokens.S_IFNDEF_KEYWORD, CPsiTokens.S_ENDIF_KEYWORD, false)
			};

	@Override
	public BracePair[] getPairs() {
		return PAIRS;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
		return false;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
		return openingBraceOffset;
	}
}
