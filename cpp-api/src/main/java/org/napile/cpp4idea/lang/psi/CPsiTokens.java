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

package org.napile.cpp4idea.lang.psi;

import consulo.cpp.preprocessor.psi.CPreprocessorTokenTypes;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.ast.TokenType;

/**
 * @author VISTALL
 * @date 2:42/10.12.2011
 */
public interface CPsiTokens extends TokenType, CPreprocessorTokenTypes
{
	IElementType IDENTIFIER = new CBaseElementType("IDENTIFIER");
	IElementType NEW_LINE = new CBaseElementType("NEW_LINE");
	// comments
	IElementType C_STYLE_COMMENT = new CBaseElementType("C_STYLE_COMMENT"); // /* comment
	IElementType END_OF_LINE_COMMENT = new CBaseElementType("END_OF_LINE_COMMENT");     // // comment

	// keywords
	IElementType NAMESPACE_KEYWORD = new CBaseElementType("NAMESPACE_KEYWORD"); // namespace
	IElementType VOID_KEYWORD = new CBaseElementType("VOID_KEYWORD"); // void
	IElementType CASE_KEYWORD = new CBaseElementType("CASE_KEYWORD"); // case
	IElementType CONTINUE_KEYWORD = new CBaseElementType("CONTINUE_KEYWORD");
	IElementType DEFAULT_KEYWORD = new CBaseElementType("DEFAULT_KEYWORD");
	IElementType DO_KEYWORD = new CBaseElementType("DO_KEYWORD");
	IElementType CONST_KEYWORD = new CBaseElementType("CONST_KEYWORD");
	IElementType SIGNED_KEYWORD = new CBaseElementType("SIGNED_KEYWORD");   //signed
	IElementType UNSIGNED_KEYWORD = new CBaseElementType("UNSIGNED_KEYWORD");
	IElementType ELSE_KEYWORD = new CBaseElementType("ELSE_KEYWORD");
	IElementType BREAK_KEYWORD = new CBaseElementType("BREAK_KEYWORD");
	IElementType RETURN_KEYWORD = new CBaseElementType("RETURN_KEYWORD");
	IElementType WHILE_KEYWORD = new CBaseElementType("WHILE_KEYWORD");
	IElementType FOR_KEYWORD = new CBaseElementType("FOR_KEYWORD");
	IElementType GOTO_KEYWORD = new CBaseElementType("GOTO_KEYWORD");
	IElementType IF_KEYWORD = new CBaseElementType("IF_KEYWORD");
	IElementType NEW_KEYWORD = new CBaseElementType("NEW_KEYWORD");
	IElementType SWITCH_KEYWORD = new CBaseElementType("SWITCH_KEYWORD");
	IElementType PUBLIC_KEYWORD = new CBaseElementType("PUBLIC_KEYWORD");
	IElementType PRIVATE_KEYWORD = new CBaseElementType("PRIVATE_KEYWORD");
	IElementType STATIC_KEYWORD = new CBaseElementType("STATIC_KEYWORD");
	IElementType EXPLICIT_KEYWORD = new CBaseElementType("EXPLICIT_KEYWORD");  //explicit
	IElementType VIRTUAL_KEYWORD = new CBaseElementType("VIRTUAL_KEYWORD");  //virtual
	IElementType TYPEDEF_KEYWORD = new CBaseElementType("TYPEDEF_KEYWORD"); // typedef
	IElementType EXTERN_KEYWORD = new CBaseElementType("EXTERN_KEYWORD"); // extern
	IElementType ENUM_KEYWORD = new CBaseElementType("ENUM_KEYWORD"); // enum
	IElementType CLASS_KEYWORD = new CBaseElementType("CLASS_KEYWORD"); // class
	IElementType STRUCT_KEYWORD = new CBaseElementType("STRUCT_KEYWORD"); // struct
	IElementType UNION_KEYWORD = new CBaseElementType("UNION_KEYWORD"); // union
	IElementType SIZEOF_KEYWORD = new CBaseElementType("SIZEOF_KEYWORD"); // sizeof

	// types
	IElementType CHAR_KEYWORD = new CBaseElementType("CHAR_KEYWORD"); // char
	IElementType BOOL_KEYWORD = new CBaseElementType("BOOL_KEYWORD"); // bool
	IElementType SHORT_KEYWORD = new CBaseElementType("SHORT_KEYWORD"); // short
	IElementType INT_KEYWORD = new CBaseElementType("INT_KEYWORD"); // int
	IElementType LONG_KEYWORD = new CBaseElementType("LONG_KEYWORD"); // long
	IElementType __INT64_KEYWORD = new CBaseElementType("__INT64_KEYWORD"); // __int64
	IElementType FLOAT_KEYWORD = new CBaseElementType("FLOAT_KEYWORD"); // float
	IElementType DOUBLE_KEYWORD = new CBaseElementType("DOUBLE_KEYWORD"); // double

	// data
	IElementType INTEGER_LITERAL = new CBaseElementType("INTEGER_LITERAL");
	IElementType LONG_LITERAL = new CBaseElementType("LONG_LITERAL");
	IElementType FLOAT_LITERAL = new CBaseElementType("FLOAT_LITERAL");
	IElementType DOUBLE_LITERAL = new CBaseElementType("DOUBLE_LITERAL");
	IElementType CHARACTER_LITERAL = new CBaseElementType("CHARACTER_LITERAL");
	IElementType STRING_LITERAL = new CBaseElementType("STRING_LITERAL");
	IElementType STRING_INCLUDE_LITERAL = new CBaseElementType("STRING_INCLUDE_LITERAL");
	IElementType BOOL_LITERAL = new CBaseElementType("BOOL_LITERAL");

	//
	IElementType LPARENTH = new CBaseElementType("LPARENTH");
	IElementType RPARENTH = new CBaseElementType("RPARENTH");
	IElementType LBRACE = new CBaseElementType("LBRACE");
	IElementType RBRACE = new CBaseElementType("RBRACE");
	IElementType LBRACKET = new CBaseElementType("LBRACKET");
	IElementType RBRACKET = new CBaseElementType("RBRACKET");
	IElementType SEMICOLON = new CBaseElementType("SEMICOLON");
	IElementType COMMA = new CBaseElementType("COMMA");
	IElementType DOT = new CBaseElementType("DOT");
	IElementType ELLIPSIS = new CBaseElementType("ELLIPSIS");
	IElementType AT = new CBaseElementType("AT");

	IElementType EQ = new CBaseElementType("EQ");
	IElementType GT = new CBaseElementType("GT");
	IElementType LT = new CBaseElementType("LT");
	IElementType EXCL = new CBaseElementType("EXCL");
	IElementType TILDE = new CBaseElementType("TILDE");
	IElementType QUEST = new CBaseElementType("QUEST");
	IElementType COLON = new CBaseElementType("COLON");
	IElementType DOUBLE_COLON = new CBaseElementType("DOUBLE_COLON");
	IElementType PLUS = new CBaseElementType("PLUS");
	IElementType MINUS = new CBaseElementType("MINUS");
	IElementType ASTERISK = new CBaseElementType("ASTERISK");
	IElementType DIV = new CBaseElementType("DIV");
	IElementType NEXT_LINE = new CBaseElementType("NEXT_LINE");  // \
	IElementType AND = new CBaseElementType("AND");
	IElementType OR = new CBaseElementType("OR");
	IElementType XOR = new CBaseElementType("XOR");
	IElementType PERC = new CBaseElementType("PERC");
	IElementType ARROW = new CBaseElementType("ARROW");

	IElementType EQEQ = new CBaseElementType("EQEQ");
	IElementType LE = new CBaseElementType("LE");
	IElementType GE = new CBaseElementType("GE");
	IElementType NE = new CBaseElementType("NE");
	IElementType ANDAND = new CBaseElementType("ANDAND");
	IElementType OROR = new CBaseElementType("OROR");
	IElementType PLUSPLUS = new CBaseElementType("PLUSPLUS");
	IElementType MINUSMINUS = new CBaseElementType("MINUSMINUS");
	IElementType LTLT = new CBaseElementType("LTLT");
	IElementType GTGT = new CBaseElementType("GTGT");
	IElementType GTGTGT = new CBaseElementType("GTGTGT");
	IElementType PLUSEQ = new CBaseElementType("PLUSEQ");
	IElementType MINUSEQ = new CBaseElementType("MINUSEQ");
	IElementType ASTERISKEQ = new CBaseElementType("ASTERISKEQ");
	IElementType DIVEQ = new CBaseElementType("DIVEQ");
	IElementType ANDEQ = new CBaseElementType("ANDEQ");
	IElementType OREQ = new CBaseElementType("OREQ");
	IElementType XOREQ = new CBaseElementType("XOREQ");
	IElementType PERCEQ = new CBaseElementType("PERCEQ");
	IElementType LTLTEQ = new CBaseElementType("LTLTEQ");
	IElementType GTGTEQ = new CBaseElementType("GTGTEQ");
	IElementType GTGTGTEQ = new CBaseElementType("GTGTGTEQ");

	TokenSet ACCESS_MODIFIERS = TokenSet.create(PUBLIC_KEYWORD, PRIVATE_KEYWORD);
	TokenSet MODIFIERS = TokenSet.create(VIRTUAL_KEYWORD, EXPLICIT_KEYWORD, STATIC_KEYWORD, CONST_KEYWORD, EXTERN_KEYWORD);
	TokenSet WHITE_SPACE_SET = TokenSet.create(WHITE_SPACE);
	TokenSet STRING_LITERAL_SET = TokenSet.create(STRING_LITERAL, CHARACTER_LITERAL);
	TokenSet COMMENT_SET = TokenSet.create(C_STYLE_COMMENT, END_OF_LINE_COMMENT);
	TokenSet LITERAL_EXPRESSION_SET = TokenSet.create(STRING_LITERAL, CHARACTER_LITERAL, INTEGER_LITERAL, LONG_KEYWORD, DOUBLE_LITERAL, FLOAT_LITERAL, BOOL_LITERAL);

	TokenSet EQUALITY_OPERATIONS = TokenSet.create(EQEQ, NE);
	TokenSet ASSIGNMENT_OPERATIONS = TokenSet.create(EQ, PLUSEQ, MINUSEQ, ASTERISKEQ, PERCEQ, LTLTEQ, GTGTEQ, GTGTGTEQ, ANDEQ, OREQ, XOREQ, DIVEQ);
	/** Unary prefix operators — includes address-of ({@code &}) and dereference ({@code *}). */
	TokenSet UNARY_OPERATIONS = TokenSet.create(PLUS, MINUS, PLUSPLUS, MINUSMINUS, TILDE, EXCL, AND, ASTERISK);
	TokenSet SHIFT_OPERATIONS = TokenSet.create(LTLT, GTGT, GTGTGT);
	TokenSet ADDITIVE_OPERATIONS = TokenSet.create(PLUS, MINUS);
	TokenSet MULTIPLICATIVE_OPERATIONS = TokenSet.create(ASTERISK, DIV, PERC);
	TokenSet RELATIONAL_OPERATIONS = TokenSet.create(LT, GT, LE, GE);

	/** Primitive type keywords and {@code void}. */
	TokenSet TYPES = TokenSet.create(
			INT_KEYWORD, __INT64_KEYWORD, BOOL_KEYWORD, CHAR_KEYWORD,
			SHORT_KEYWORD, LONG_KEYWORD, FLOAT_KEYWORD, DOUBLE_KEYWORD, VOID_KEYWORD
	);

	/** Aggregate-type starters: {@code struct} and {@code union}. */
	TokenSet AGGREGATE_TYPES = TokenSet.create(STRUCT_KEYWORD, UNION_KEYWORD);

	TokenSet OPERATION_SET = TokenSet.create
			(
					EQ, GT, LT, EXCL, TILDE, QUEST, COLON, PLUS, MINUS, ASTERISK, DIV, AND, OR, XOR,
					PERC, EQEQ, LE, GE, NE, ANDAND, OROR, PLUSPLUS, MINUSMINUS, LTLT, GTGT, GTGTGT,
					PLUSEQ, MINUSEQ, ASTERISKEQ, DIVEQ, ANDEQ, OREQ, XOREQ, PERCEQ, LTLTEQ, GTGTEQ, GTGTGTEQ
			);

	TokenSet KEYWORD_SET = TokenSet.create
			(
					BREAK_KEYWORD, CASE_KEYWORD, CONTINUE_KEYWORD,
					DEFAULT_KEYWORD, DO_KEYWORD, ELSE_KEYWORD,
					FOR_KEYWORD, GOTO_KEYWORD, IF_KEYWORD,
					NEW_KEYWORD, PUBLIC_KEYWORD, RETURN_KEYWORD,
					SWITCH_KEYWORD, VOID_KEYWORD, WHILE_KEYWORD,
					CONST_KEYWORD, TYPEDEF_KEYWORD, SIGNED_KEYWORD, UNSIGNED_KEYWORD,
					CHAR_KEYWORD, SHORT_KEYWORD, INT_KEYWORD, LONG_KEYWORD, __INT64_KEYWORD,
					FLOAT_KEYWORD, DOUBLE_KEYWORD,
					S_INCLUDE_KEYWORD, S_DEFINE_KEYWORD, S_IFDEF_KEYWORD, S_IFNDEF_KEYWORD, S_ELSE_KEYWORD, S_ENDIF_KEYWORD,
					ENUM_KEYWORD, EXTERN_KEYWORD, BOOL_KEYWORD,
					PRIVATE_KEYWORD, STATIC_KEYWORD, CLASS_KEYWORD, NAMESPACE_KEYWORD,
					VIRTUAL_KEYWORD, EXPLICIT_KEYWORD,
					STRUCT_KEYWORD, UNION_KEYWORD, SIZEOF_KEYWORD
			);
}
