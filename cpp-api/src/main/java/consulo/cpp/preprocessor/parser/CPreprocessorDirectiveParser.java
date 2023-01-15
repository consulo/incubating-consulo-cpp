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

import consulo.cpp.preprocessor.psi.*;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @date 13:34/29.12.12
 */
public class CPreprocessorDirectiveParser extends CPreprocessorParserHelper
{
	public static final int EAT_LAST_END_IF = 1 << 0;

	public static void parse(@NotNull PsiBuilder builder, int f)
	{
		while(!builder.eof())
		{
			skipLines(builder);

			if(builder.getTokenType() == S_INCLUDE_KEYWORD)
			{
				parseInclude(builder);
			}
			else if(builder.getTokenType() == S_DEFINE_KEYWORD)
			{
				parseDefine(builder);
			}
			else if(builder.getTokenType() == S_IFNDEF_KEYWORD || builder.getTokenType() == S_IFDEF_KEYWORD)
			{
				parseIf(builder);
			}
			else if(builder.getTokenType() == S_ENDIF_KEYWORD || builder.getTokenType() == S_ELSE_KEYWORD)
			{
				if(isSet(f, EAT_LAST_END_IF))
				{
					error(builder, "S_IFDEF.or.S_IFNDEF.expected");
					advanceLexerAndSkipLines(builder);
				}
				else
				{
					break;
				}
			}
			else
			{
				builder.advanceLexer();
			}
		}
	}

	public static void parseInclude(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		IElementType nextElement = builder.lookAhead(1);

		if(nextElement != STRING_LITERAL && nextElement != STRING_INCLUDE_LITERAL)
		{
			builder.error("Incorrect include name");
		}
		else
		{
			builder.advanceLexer();
		}

		builder.advanceLexer();

		done(marker, nextElement == STRING_LITERAL ? CPsiSharpInclude.class : CPsiSharpIndepInclude.class);

		skipLines(builder);
	}

	public static void parseDefine(PsiBuilder builder)
	{
		PsiBuilder.Marker maker = builder.mark();

		// #define
		advanceLexerAndSkipLines(builder);

		// var name
		builder.advanceLexer();

		PsiBuilder.Marker valueMarker = builder.mark();

		while(!builder.eof())
		{
			if(builder.getTokenType() == NEW_LINE)
			{
				break;
			}

			if(builder.getTokenType() == NEXT_LINE)
			{
				builder.advanceLexer();
			}

			builder.advanceLexer();
		}

		done(valueMarker, CPsiSharpDefineValue.class);

		done(maker, CPreprocessorDefineDirective.class);

		skipLines(builder);
	}

	public static void parseIf(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		builder.advanceLexer();

		if(builder.getTokenType() == IDENTIFIER)
		{
			doneOneToken(builder, CPreprocessorMacroReference.class);
		}
		else
		{
			error(builder, "IDENTIFIER.expected");
		}

		builder.advanceLexer();

		if(builder.getTokenType() == NEW_LINE)
		{
			builder.advanceLexer();
		}

		skipLines(builder);

		if(builder.getTokenType() != S_ENDIF_KEYWORD)
		{
			PsiBuilder.Marker bodyMarker = builder.mark();

			while(!builder.eof())
			{
				parse(builder, 0);

				if(builder.getTokenType() == S_ENDIF_KEYWORD || builder.getTokenType() == S_ELSE_KEYWORD)
				{
					break;
				}
			}

			done(bodyMarker, CPsiSharpIfBody.class);

			if(builder.getTokenType() == S_ELSE_KEYWORD)
			{
				builder.advanceLexer();

				PsiBuilder.Marker elseBody = builder.mark();

				while(!builder.eof())
				{
					parse(builder, 0);

					if(builder.getTokenType() == S_ENDIF_KEYWORD || builder.getTokenType() == S_ELSE_KEYWORD)
					{
						break;
					}
				}
				done(elseBody, CPsiSharpIfBody.class);
			}
		}
		checkMatchesWithoutLines(builder, S_ENDIF_KEYWORD, "S_END_IF.expected");

		done(marker, CPreprocessorIfBlock.class);
	}
}
