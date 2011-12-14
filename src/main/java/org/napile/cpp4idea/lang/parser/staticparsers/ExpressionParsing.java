/*
	 * Copyright 2000-2005 JetBrains s.r.o.
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
package org.napile.cpp4idea.lang.parser.staticparsers;

import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.*;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 10:54/14.12.2011
 */
public class ExpressionParsing extends CommonParsing
{
	private static final Logger LOG = Logger.getInstance("#com.intellij.lang.javascript.parsing.ExpressionParsing");

	private static boolean parsePrimaryExpression(PsiBuilder builder)
	{
		final IElementType firstToken = builder.getTokenType();
		if(firstToken == IDENTIFIER)
		{
			buildTokenElement(CPsiReferenceExpression.class, builder);
			return true;
		}
		else if(LITERAL_EXPRESSION_SET.contains(firstToken))
		{
			String errorMessage = validateLiteral(builder);

			buildTokenElement(CPsiLiteralExpression.class, builder);
			if(errorMessage != null)
				builder.error(errorMessage);

			return true;
		}
		else if(firstToken == LPARENTH)
		{
			parseParenthesizedExpression(builder);
			return true;
		}
		else if(firstToken == LBRACKET)
		{
			parseArrayLiteralExpression(builder);
			return true;
		}
		/*else if(firstToken == FUNCTION_KEYWORD)
		{
			FunctionParsing.parseFunctionExpression(builder);
			return true;
		} */
		else
		{
			return false;
		}
	}

	@Nullable
	private static String validateLiteral(final PsiBuilder builder)
	{
		final IElementType ttype = builder.getTokenType();
		if(ttype == STRING_LITERAL)
		{
			final String ttext = builder.getTokenText();
			assert ttext != null;

			if(lastSymbolEscaped(ttext) ||
					ttext.startsWith("\"") && (!ttext.endsWith("\"") || ttext.length() == 1) ||
					ttext.startsWith("\'") && (!ttext.endsWith("\'") || ttext.length() == 1))
			{
				return "Unclosed string literal";
			}
		}

		return null;
	}

	private static boolean lastSymbolEscaped(String text)
	{
		boolean escapes = false;
		boolean escaped = true;
		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if(escapes)
			{
				escapes = false;
				escaped = true;
				continue;
			}
			if(c == '\\')
			{
				escapes = true;
			}
			escaped = false;
		}
		return escapes || escaped;
	}

	private static void parseArrayLiteralExpression(final PsiBuilder builder)
	{
		LOG.assertTrue(builder.getTokenType() == LBRACKET);
		final PsiBuilder.Marker expr = builder.mark();
		builder.advanceLexer();
		while(builder.getTokenType() != RBRACKET)
		{
			if(builder.getTokenType() == COMMA)
			{
				builder.advanceLexer();
			}
			else if(!parseAssignmentExpression(builder))
			{
				builder.error("expression or , or ] expected");
				break;
			}
		}
		checkMatches(builder, RBRACKET, "] expected");
		done(expr, CPsiArrayLiteralExpression.class);
	}

	private static void parseParenthesizedExpression(final PsiBuilder builder)
	{
		LOG.assertTrue(builder.getTokenType() == LPARENTH);
		final PsiBuilder.Marker expr = builder.mark();
		builder.advanceLexer();

		parseExpression(builder);

		checkMatches(builder, RPARENTH, ") expected");

		done(expr, CPsiParenthesizedExpression.class);
	}

	private static boolean parseMemberExpression(PsiBuilder builder, boolean allowCallSyntax)
	{
		PsiBuilder.Marker expr = builder.mark();
		final boolean isNew;
		if(builder.getTokenType() == NEW_KEYWORD)
		{
			isNew = true;
			parseNewExpression(builder);
		}
		else
		{
			isNew = false;
			if(!parsePrimaryExpression(builder))
			{
				expr.drop();
				return false;
			}
		}

		while(true)
		{
			final IElementType tokenType = builder.getTokenType();
			if(tokenType == DOT)
			{
				builder.advanceLexer();
				checkMatches(builder, IDENTIFIER, "name expected");
				done(expr, CPsiReferenceExpression.class);
				expr = expr.precede();
			}
			else if(tokenType == LBRACKET)
			{
				builder.advanceLexer();
				parseExpression(builder);
				checkMatches(builder, RBRACKET, "] expected");
				done(expr, CPsiIndexedPropertyAccessExpression.class);
				expr = expr.precede();
			}
			else if(allowCallSyntax && tokenType == LPARENTH)
			{
				parseArgumentList(builder);
				done(expr, isNew ? CPsiNewExpression.class : CPsiCallExpression.class);
				expr = expr.precede();
			}
			else
			{
				expr.drop();
				break;
			}
		}

		return true;
	}

	private static void parseNewExpression(PsiBuilder builder)
	{
		LOG.assertTrue(builder.getTokenType() == NEW_KEYWORD);
		builder.advanceLexer();

		if(!parseMemberExpression(builder, false))
			builder.error("Expression expected");

		if(builder.getTokenType() == LPARENTH)
			parseArgumentList(builder);
	}

	private static void parseArgumentList(final PsiBuilder builder)
	{
		LOG.assertTrue(builder.getTokenType() == LPARENTH);
		final PsiBuilder.Marker arglist = builder.mark();
		builder.advanceLexer();
		boolean first = true;
		while(builder.getTokenType() != RPARENTH)
		{
			if(first)
			{
				first = false;
			}
			else
			{
				if(builder.getTokenType() == COMMA)
				{
					builder.advanceLexer();
				}
				else
				{
					builder.error(", or ) expected");
					break;
				}
			}
			if(!parseAssignmentExpression(builder))
			{
				builder.error("expression expected");
			}
		}

		checkMatches(builder, RPARENTH, ") expected");

		done(arglist, CPsiArgumentList.class);
	}

	public static void parseExpression(PsiBuilder builder)
	{
		if(!parseExpressionOptional(builder))
			builder.error("expression expected");
	}

	public static boolean parseAssignmentExpressionNoIn(final PsiBuilder builder)
	{
		return parseAssignmentExpression(builder, false);
	}

	public static boolean parseAssignmentExpression(final PsiBuilder builder)
	{
		return parseAssignmentExpression(builder, true);
	}

	private static boolean parseAssignmentExpression(final PsiBuilder builder, boolean allowIn)
	{
		final PsiBuilder.Marker expr = builder.mark();
		if(!parseConditionalExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		if(ASSIGNMENT_OPERATIONS.contains(builder.getTokenType()))
		{
			builder.advanceLexer();
			if(!parseAssignmentExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiAssignmentExpression.class);
		}
		else
		{
			expr.drop();
		}
		return true;
	}

	private static boolean parseConditionalExpression(final PsiBuilder builder, final boolean allowIn)
	{
		final PsiBuilder.Marker expr = builder.mark();
		if(!parseORExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		if(builder.getTokenType() == QUEST)
		{
			builder.advanceLexer();
			if(!parseAssignmentExpression(builder, allowIn))
				builder.error("expression expected");

			checkMatches(builder, COLON, ": expected");

			if(!parseAssignmentExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiConditionalExpression.class);
		}
		else
		{
			expr.drop();
		}
		return true;
	}

	private static boolean parseORExpression(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseANDExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		while(builder.getTokenType() == OROR)
		{
			builder.advanceLexer();
			if(!parseANDExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseANDExpression(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseBitwiseORExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		while(builder.getTokenType() == ANDAND)
		{
			builder.advanceLexer();
			if(!parseBitwiseORExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseBitwiseORExpression(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseBitwiseXORExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		while(builder.getTokenType() == OR)
		{
			builder.advanceLexer();
			if(!parseBitwiseXORExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseBitwiseXORExpression(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseBitwiseANDExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		while(builder.getTokenType() == XOR)
		{
			builder.advanceLexer();
			if(!parseBitwiseANDExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseBitwiseANDExpression(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseEqualityExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		while(builder.getTokenType() == AND)
		{
			builder.advanceLexer();
			if(!parseEqualityExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseEqualityExpression(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseRelationalExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		while(EQUALITY_OPERATIONS.contains(builder.getTokenType()))
		{
			builder.advanceLexer();
			if(!parseRelationalExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseRelationalExpression(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseShiftExpression(builder))
		{
			expr.drop();
			return false;
		}
		while(RELATIONAL_OPERATIONS.contains(builder.getTokenType()) && allowIn)
		{
			builder.advanceLexer();
			if(!parseShiftExpression(builder))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseShiftExpression(final PsiBuilder builder)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseAdditiveExpression(builder))
		{
			expr.drop();
			return false;
		}
		while(SHIFT_OPERATIONS.contains(builder.getTokenType()))
		{
			builder.advanceLexer();
			if(!parseAdditiveExpression(builder))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseAdditiveExpression(final PsiBuilder builder)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseMultiplicativeExpression(builder))
		{
			expr.drop();
			return false;
		}
		while(ADDITIVE_OPERATIONS.contains(builder.getTokenType()))
		{
			builder.advanceLexer();
			if(!parseMultiplicativeExpression(builder))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseMultiplicativeExpression(final PsiBuilder builder)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseUnaryExpression(builder))
		{
			expr.drop();
			return false;
		}

		while(MULTIPLICATIVE_OPERATIONS.contains(builder.getTokenType()))
		{
			builder.advanceLexer();
			if(!parseUnaryExpression(builder))
				builder.error("expression expected");

			done(expr, CPsiBinaryExpression.class);
			expr = expr.precede();
		}

		expr.drop();
		return true;
	}

	private static boolean parseUnaryExpression(final PsiBuilder builder)
	{
		final IElementType tokenType = builder.getTokenType();
		if(UNARY_OPERATIONS.contains(tokenType))
		{
			final PsiBuilder.Marker expr = builder.mark();
			builder.advanceLexer();
			if(!parseUnaryExpression(builder))
				builder.error("Expression expected");

			done(expr, CPsiPrefixExpression.class);
			return true;
		}
		else
		{
			return parsePostfixExpression(builder);
		}
	}

	private static boolean parsePostfixExpression(PsiBuilder builder)
	{
		final PsiBuilder.Marker expr = builder.mark();
		if(!parseMemberExpression(builder, true))
		{
			expr.drop();
			return false;
		}

		final IElementType tokenType = builder.getTokenType();
		if(tokenType == PLUSPLUS || tokenType == MINUSMINUS)
		{
			builder.advanceLexer();

			done(expr, CPsiPostfixExpression.class);
		}
		else
			expr.drop();

		return true;
	}

	public static boolean parseExpressionOptional(final PsiBuilder builder)
	{
		return parseExpressionOptional(builder, true);
	}

	public static boolean parseExpressionOptionalNoIn(final PsiBuilder builder)
	{
		return parseExpressionOptional(builder, false);
	}

	public static boolean parseExpressionOptional(final PsiBuilder builder, final boolean allowIn)
	{
		PsiBuilder.Marker expr = builder.mark();
		if(!parseAssignmentExpression(builder, allowIn))
		{
			expr.drop();
			return false;
		}

		while(builder.getTokenType() == COMMA)
		{
			builder.advanceLexer();
			if(!parseAssignmentExpression(builder, allowIn))
				builder.error("expression expected");

			done(expr, CPsiCommaExpression.class);
			expr = expr.precede();
		}

		expr.drop();

		return true;
	}    

	private static void buildTokenElement(Class<? extends CPsiElement> clazz, PsiBuilder builder)
	{
		final PsiBuilder.Marker marker = builder.mark();

		builder.advanceLexer();

		done(marker, clazz);

		skipLines(builder);
	}
}
