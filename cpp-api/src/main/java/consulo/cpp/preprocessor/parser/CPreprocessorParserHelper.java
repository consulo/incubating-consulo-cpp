/*
 * Copyright 2010-2012 napile.org
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

package consulo.cpp.preprocessor.parser;

import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;
import consulo.cpp.preprocessor.psi.CSharpTokenElements;
import consulo.language.psi.PsiElement;
import org.jetbrains.annotations.PropertyKey;
import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 13:36/29.12.12
 */
public class CPreprocessorParserHelper implements CPsiTokens {
	public static boolean isSet(int val, int constant) {
		return (val & constant) != 0;
	}

	public static void advanceLexerAndSkipLines(PsiBuilder builder) {
		builder.advanceLexer();

		skipLines(builder);
	}

	public static void skipLines(PsiBuilder builder) {
		while (builder.getTokenType() == NEW_LINE)
			builder.advanceLexer();
	}

	public static void doneOneToken(PsiBuilder builder, Class<? extends PsiElement> clazz) {
		PsiBuilder.Marker marker = builder.mark();

		builder.advanceLexer();

		done(marker, clazz);
	}

	protected static void checkMatches(final PsiBuilder builder, final IElementType token, @PropertyKey(resourceBundle = CBundle.PATH_TO_BUNDLE) final String message) {
		if (builder.getTokenType() == token) {
			advanceLexerAndSkipLines(builder);
		} else {
			builder.error(CBundle.message(message));
		}
	}

	protected static void checkMatchesWithoutLines(final PsiBuilder builder, final IElementType token, @PropertyKey(resourceBundle = CBundle.PATH_TO_BUNDLE) final String message) {
		if (builder.getTokenType() == token) {
			builder.advanceLexer();
		} else {
			builder.error(CBundle.message(message));
		}
	}

	protected static void error(final PsiBuilder builder, @PropertyKey(resourceBundle = CBundle.PATH_TO_BUNDLE) final String message) {
		builder.error(CBundle.message(message));
	}

	public static void done(PsiBuilder.Marker marker, Class<? extends PsiElement> clazz) {
		marker.done(CSharpTokenElements.element(clazz));
	}
}
