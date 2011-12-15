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

package org.napile.cpp4idea.lang.parser.secondparsing;

import org.jetbrains.annotations.PropertyKey;
import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.parser.CTokenElements;
import org.napile.cpp4idea.lang.parser.firstparsing.SharpDefineKeyword;
import org.napile.cpp4idea.lang.parser.firstparsing.SharpIfdefKeyword;
import org.napile.cpp4idea.lang.parser.firstparsing.SharpIncludeKeyword;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psi.CPsiDeclarationMethod;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import org.napile.cpp4idea.lang.psi.CPsiField;
import org.napile.cpp4idea.lang.psi.CPsiImplentingMethod;
import org.napile.cpp4idea.lang.psi.CPsiTypeRef;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 14:26/11.12.2011
 */
public class CommonParsing implements CTokenType
{
	public static void parseElement(PsiBuilder builder)
	{
		while(!builder.eof())
		{
			skipLines(builder);

			if(builder.getTokenType() == S_INCLUDE_KEYWORD)
				SharpIncludeKeyword.parse(builder);
			else if(builder.getTokenType() == S_DEFINE_KEYWORD)
				SharpDefineKeyword.parse(builder);
			else if(builder.getTokenType() == S_IFNDEF_KEYWORD || builder.getTokenType() == S_IFDEF_KEYWORD)
				SharpIfdefKeyword.parseIf(builder);
			else if(builder.getTokenType() == TYPEDEF_KEYWORD)
				TypeDefParsing.parse(builder);
			else if(builder.getTokenType() == S_ENDIF_KEYWORD)
				advanceLexerAndSkipLines(builder);
			else if(builder.getTokenType() == RBRACE)
				advanceLexerAndSkipLines(builder);
			else if(builder.getTokenType() == EXTERN_KEYWORD)
			{
				//TODO [VISTALL]
				advanceLexerAndSkipLines(builder);
			}
			else if(builder.getTokenType() == ENUM_KEYWORD)
				EnumParsing.parse(builder);
			else
				parseMethodOrField(builder);
		}
	}

	public static void parseTypeRef(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		if(builder != ELLIPSIS)
		{
			// *char
			// const char
			if(builder.getTokenType() == ASTERISK || builder.getTokenType() == CONST_KEYWORD || builder.getTokenType() == SIGNED_KEYWORD || builder.getTokenType() == UNSIGNED_KEYWORD)
				advanceLexerAndSkipLines(builder);

			advanceLexerAndSkipLines(builder);

			// char**
			while(builder.getTokenType() == ASTERISK)
				advanceLexerAndSkipLines(builder);
		}
		else
			builder.advanceLexer();

		done(marker, CPsiTypeRef.class);

		skipLines(builder);
	}

	public static void parseCompilerVariable(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		builder.advanceLexer();

		done(marker, CPsiCompilerVariable.class);
	}

	public static void parseMethodOrField(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		parseTypeRef(builder);

		IElementType methodName = builder.getTokenType();
		if(methodName != IDENTIFIER)
		{
			builder.error("Field name expected");
			done(marker, CPsiField.class);
			return;
		}
		else
			advanceLexerAndSkipLines(builder);

		if(builder.getTokenType() == LPARENTH)
		{
			ParameterListParsing.parseParameterList(builder);

			if(builder.getTokenType() != SEMICOLON)
			{
				CodeBlockParsing.parseCodeBlock(builder);

				done(marker, CPsiImplentingMethod.class);
			}
			else
			{
				builder.advanceLexer();

				done(marker, CPsiDeclarationMethod.class);

				skipLines(builder);
			}
		}
		else
		{
			if(builder.getTokenType() != SEMICOLON)
			{
				if(builder.getTokenType() != EQ)
					builder.error(CBundle.message("EQ.expected"));
				else
				{
					advanceLexerAndSkipLines(builder);

					ExpressionParsing.parseExpression(builder);

					if(builder.getTokenType() != SEMICOLON)
						builder.error(CBundle.message("SEMICOLON.expected"));

					builder.advanceLexer();
				}
			}
			else
				builder.advanceLexer();

			done(marker, CPsiField.class);

			skipLines(builder);
		}
	}

	public static void advanceLexerAndSkipLines(PsiBuilder builder)
	{
		builder.advanceLexer();

		skipLines(builder);
	}

	public static void skipLines(PsiBuilder builder)
	{
		while(builder.getTokenType() == NEW_LINE)
			builder.advanceLexer();
	}

	public static IElementType lookAheadIgnoreLines(PsiBuilder builder, int step)
	{
		while(!builder.eof())
		{
			IElementType elementType = builder.lookAhead(step);
			if(elementType == null)
				break;

			if(elementType == NEW_LINE)
				step ++;
			else
				return elementType;
		}
		return null;
	}

	protected static void checkMatches(final PsiBuilder builder, final IElementType token, @PropertyKey(resourceBundle = CBundle.PATH_TO_BUNDLE) final String message)
	{
		if(builder.getTokenType() == token)
			advanceLexerAndSkipLines(builder);
		else
			builder.error(CBundle.message(message));
	}

	public static void done(PsiBuilder.Marker marker, Class<? extends CPsiElement> clazz)
	{
		marker.done(CTokenElements.element(clazz));
	}
}
