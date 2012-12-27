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

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @date 2:42/10.12.2011
 */
public interface CTokenType extends TokenType
{
	IElementType IDENTIFIER = new CTokenImpl("IDENTIFIER");
	IElementType NEW_LINE = new CTokenImpl("NEW_LINE");
	// comments
	IElementType C_STYLE_COMMENT = new CTokenImpl("C_STYLE_COMMENT"); // // comment
	IElementType END_OF_LINE_COMMENT = new CTokenImpl("END_OF_LINE_COMMENT");	 // /* comment

	// sharp keywords
	IElementType S_INCLUDE_KEYWORD = new CTokenImpl("S_INCLUDE_KEYWORD"); // #include
	IElementType S_DEFINE_KEYWORD = new CTokenImpl("S_DEFINE_KEYWORD"); // #define
	IElementType S_IFDEF_KEYWORD = new CTokenImpl("S_IFDEF_KEYWORD"); // #ifdef
	IElementType S_IFNDEF_KEYWORD = new CTokenImpl("S_IFNDEF_KEYWORD"); // #ifndef
	IElementType S_ENDIF_KEYWORD = new CTokenImpl("S_ENDIF_KEYWORD"); // #endif
	IElementType S_PRAGMA_KEYWORD = new CTokenImpl("S_PRAGMA_KEYWORD"); // #pragma

	// keywords
	IElementType VOID_KEYWORD = new CTokenImpl("VOID_KEYWORD"); // void
	IElementType CASE_KEYWORD = new CTokenImpl("CASE_KEYWORD"); // case
	IElementType CONTINUE_KEYWORD = new CTokenImpl("CONTINUE_KEYWORD");
	IElementType DEFAULT_KEYWORD = new CTokenImpl("DEFAULT_KEYWORD");
	IElementType DO_KEYWORD = new CTokenImpl("DO_KEYWORD");
	IElementType CONST_KEYWORD = new CTokenImpl("CONST_KEYWORD");
	IElementType SIGNED_KEYWORD = new CTokenImpl("SIGNED_KEYWORD");   //signed
	IElementType UNSIGNED_KEYWORD = new CTokenImpl("UNSIGNED_KEYWORD");   //signed
	IElementType ELSE_KEYWORD = new CTokenImpl("ELSE_KEYWORD");
	IElementType BREAK_KEYWORD = new CTokenImpl("BREAK_KEYWORD");
	IElementType RETURN_KEYWORD = new CTokenImpl("RETURN_KEYWORD");
	IElementType WHILE_KEYWORD = new CTokenImpl("WHILE_KEYWORD");
	IElementType FOR_KEYWORD = new CTokenImpl("FOR_KEYWORD");
	IElementType GOTO_KEYWORD = new CTokenImpl("GOTO_KEYWORD");
	IElementType IF_KEYWORD = new CTokenImpl("IF_KEYWORD");
	IElementType NEW_KEYWORD = new CTokenImpl("NEW_KEYWORD");
	IElementType SWITCH_KEYWORD = new CTokenImpl("SWITCH_KEYWORD");
	IElementType PUBLIC_KEYWORD = new CTokenImpl("PUBLIC_KEYWORD");
	IElementType TYPEDEF_KEYWORD = new CTokenImpl("TYPEDEF_KEYWORD"); // typedef
	IElementType EXTERN_KEYWORD = new CTokenImpl("EXTERN_KEYWORD"); // extern
	IElementType ENUM_KEYWORD = new CTokenImpl("ENUM_KEYWORD"); // enum

	// types
	IElementType CHAR_KEYWORD = new CTokenImpl("CHAR_KEYWORD"); // char
	IElementType LONG_KEYWORD = new CTokenImpl("LONG_KEYWORD"); // long
	IElementType __INT64_KEYWORD = new CTokenImpl("__INT64_KEYWORD"); // __int64
	IElementType INT_KEYWORD = new CTokenImpl("INT_KEYWORD"); // int

	// data
	IElementType INTEGER_LITERAL = new CTokenImpl("INTEGER_LITERAL");
	IElementType LONG_LITERAL = new CTokenImpl("LONG_LITERAL");
	IElementType FLOAT_LITERAL = new CTokenImpl("FLOAT_LITERAL");
	IElementType DOUBLE_LITERAL = new CTokenImpl("DOUBLE_LITERAL");
	IElementType CHARACTER_LITERAL = new CTokenImpl("CHARACTER_LITERAL");
	IElementType STRING_LITERAL = new CTokenImpl("STRING_LITERAL");
	IElementType STRING_INCLUDE_LITERAL = new CTokenImpl("STRING_INCLUDE_LITERAL");

	//
	IElementType LPARENTH = new CTokenImpl("LPARENTH");
	IElementType RPARENTH = new CTokenImpl("RPARENTH");
	IElementType LBRACE = new CTokenImpl("LBRACE");
	IElementType RBRACE = new CTokenImpl("RBRACE");
	IElementType LBRACKET = new CTokenImpl("LBRACKET");
	IElementType RBRACKET = new CTokenImpl("RBRACKET");
	IElementType SEMICOLON = new CTokenImpl("SEMICOLON");
	IElementType COMMA = new CTokenImpl("COMMA");
	IElementType DOT = new CTokenImpl("DOT");
	IElementType ELLIPSIS = new CTokenImpl("ELLIPSIS");
	IElementType AT = new CTokenImpl("AT");

	IElementType EQ = new CTokenImpl("EQ");
	IElementType GT = new CTokenImpl("GT");
	IElementType LT = new CTokenImpl("LT");
	IElementType EXCL = new CTokenImpl("EXCL");
	IElementType TILDE = new CTokenImpl("TILDE");
	IElementType QUEST = new CTokenImpl("QUEST");
	IElementType COLON = new CTokenImpl("COLON");
	IElementType PLUS = new CTokenImpl("PLUS");
	IElementType MINUS = new CTokenImpl("MINUS");
	IElementType ASTERISK = new CTokenImpl("ASTERISK");
	IElementType DIV = new CTokenImpl("DIV");
	IElementType NEXT_LINE = new CTokenImpl("NEXT_LINE");  // \
	IElementType AND = new CTokenImpl("AND");
	IElementType OR = new CTokenImpl("OR");
	IElementType XOR = new CTokenImpl("XOR");
	IElementType PERC = new CTokenImpl("PERC");

	IElementType EQEQ = new CTokenImpl("EQEQ");
	IElementType LE = new CTokenImpl("LE");
	IElementType GE = new CTokenImpl("GE");
	IElementType NE = new CTokenImpl("NE");
	IElementType ANDAND = new CTokenImpl("ANDAND");
	IElementType OROR = new CTokenImpl("OROR");
	IElementType PLUSPLUS = new CTokenImpl("PLUSPLUS");
	IElementType MINUSMINUS = new CTokenImpl("MINUSMINUS");
	IElementType LTLT = new CTokenImpl("LTLT");
	IElementType GTGT = new CTokenImpl("GTGT");
	IElementType GTGTGT = new CTokenImpl("GTGTGT");
	IElementType PLUSEQ = new CTokenImpl("PLUSEQ");
	IElementType MINUSEQ = new CTokenImpl("MINUSEQ");
	IElementType ASTERISKEQ = new CTokenImpl("ASTERISKEQ");
	IElementType DIVEQ = new CTokenImpl("DIVEQ");
	IElementType ANDEQ = new CTokenImpl("ANDEQ");
	IElementType OREQ = new CTokenImpl("OREQ");
	IElementType XOREQ = new CTokenImpl("XOREQ");
	IElementType PERCEQ = new CTokenImpl("PERCEQ");
	IElementType LTLTEQ = new CTokenImpl("LTLTEQ");
	IElementType GTGTEQ = new CTokenImpl("GTGTEQ");
	IElementType GTGTGTEQ = new CTokenImpl("GTGTGTEQ");

	TokenSet WHITE_SPACE_SET = TokenSet.create(WHITE_SPACE);
	TokenSet STRING_LITERAL_SET = TokenSet.create(STRING_LITERAL, CHARACTER_LITERAL);
	TokenSet COMMENT_SET = TokenSet.create(C_STYLE_COMMENT, END_OF_LINE_COMMENT);
	TokenSet LITERAL_EXPRESSION_SET = TokenSet.create(STRING_LITERAL, CHARACTER_LITERAL, INTEGER_LITERAL, LONG_KEYWORD, DOUBLE_LITERAL, FLOAT_LITERAL);

	TokenSet EQUALITY_OPERATIONS = TokenSet.create(EQEQ, NE);
	TokenSet ASSIGNMENT_OPERATIONS = TokenSet.create(EQ, PLUSEQ, MINUSEQ, ASTERISKEQ, PERCEQ, LTLTEQ, GTGTEQ, GTGTGTEQ, ANDEQ, OREQ, XOREQ, DIVEQ);
	TokenSet UNARY_OPERATIONS = TokenSet.create(PLUS, MINUS, PLUSPLUS, MINUSMINUS, TILDE, EXCL);
	TokenSet SHIFT_OPERATIONS = TokenSet.create(LTLT, GTGT, GTGTGT);
	TokenSet ADDITIVE_OPERATIONS = TokenSet.create(PLUS, MINUS);
	TokenSet MULTIPLICATIVE_OPERATIONS = TokenSet.create(ASTERISK, DIV, PERC);
	TokenSet RELATIONAL_OPERATIONS = TokenSet.create(LT, GT, LE, GE);

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
		CHAR_KEYWORD, __INT64_KEYWORD, LONG_KEYWORD,
		S_INCLUDE_KEYWORD, S_DEFINE_KEYWORD, S_IFDEF_KEYWORD, S_IFNDEF_KEYWORD, S_ENDIF_KEYWORD,
		ENUM_KEYWORD, EXTERN_KEYWORD, INT_KEYWORD
	);
}
