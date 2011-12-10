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
 *    limitations under the License.
 */

package org.napile.cpp4idea.lang.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.tree.java.IJavaElementType;
import com.intellij.psi.tree.java.IKeywordElementType;

/**
 * @author VISTALL
 * @date 2:42/10.12.2011
 */
public interface CTokenType extends TokenType
{
	IElementType IDENTIFIER = new IJavaElementType("IDENTIFIER");
	// comments
	IElementType C_STYLE_COMMENT = new IJavaElementType("C_STYLE_COMMENT"); // // comment
	IElementType END_OF_LINE_COMMENT = new IJavaElementType("END_OF_LINE_COMMENT");	 // /* comment
	// keywords
	IElementType VOID_KEYWORD = new IJavaElementType("VOID_KEYWORD"); // void
	IElementType CASE_KEYWORD = new IKeywordElementType("CASE_KEYWORD"); // case
	IElementType CONTINUE_KEYWORD = new IKeywordElementType("CONTINUE_KEYWORD");
	IElementType DEFAULT_KEYWORD = new IKeywordElementType("DEFAULT_KEYWORD");
	IElementType DO_KEYWORD = new IKeywordElementType("DO_KEYWORD");

	// data
	IElementType INTEGER_LITERAL = new IJavaElementType("INTEGER_LITERAL");
	IElementType LONG_LITERAL = new IJavaElementType("LONG_LITERAL");
	IElementType FLOAT_LITERAL = new IJavaElementType("FLOAT_LITERAL");
	IElementType DOUBLE_LITERAL = new IJavaElementType("DOUBLE_LITERAL");
	IElementType CHARACTER_LITERAL = new IJavaElementType("CHARACTER_LITERAL");
	IElementType STRING_LITERAL = new IJavaElementType("STRING_LITERAL");
	IElementType ELSE_KEYWORD = new IKeywordElementType("ELSE_KEYWORD");
	IElementType BREAK_KEYWORD = new IKeywordElementType("BREAK_KEYWORD");
	IElementType RETURN_KEYWORD = new IKeywordElementType("RETURN_KEYWORD");
	IElementType WHILE_KEYWORD = new IKeywordElementType("WHILE_KEYWORD");
	IElementType FOR_KEYWORD = new IKeywordElementType("FOR_KEYWORD");
	IElementType GOTO_KEYWORD = new IKeywordElementType("GOTO_KEYWORD");
	IElementType IF_KEYWORD = new IKeywordElementType("IF_KEYWORD");
	IElementType NEW_KEYWORD = new IKeywordElementType("NEW_KEYWORD");
	IElementType SWITCH_KEYWORD = new IKeywordElementType("SWITCH_KEYWORD");
	IElementType PUBLIC_KEYWORD = new IKeywordElementType("PUBLIC_KEYWORD");

	//
	IElementType LPARENTH = new IJavaElementType("LPARENTH");
	IElementType RPARENTH = new IJavaElementType("RPARENTH");
	IElementType LBRACE = new IJavaElementType("LBRACE");
	IElementType RBRACE = new IJavaElementType("RBRACE");
	IElementType LBRACKET = new IJavaElementType("LBRACKET");
	IElementType RBRACKET = new IJavaElementType("RBRACKET");
	IElementType SEMICOLON = new IJavaElementType("SEMICOLON");
	IElementType COMMA = new IJavaElementType("COMMA");
	IElementType DOT = new IJavaElementType("DOT");
	IElementType ELLIPSIS = new IJavaElementType("ELLIPSIS");
	IElementType AT = new IJavaElementType("AT");

	IElementType EQ = new IJavaElementType("EQ");
	IElementType GT = new IJavaElementType("GT");
	IElementType LT = new IJavaElementType("LT");
	IElementType EXCL = new IJavaElementType("EXCL");
	IElementType TILDE = new IJavaElementType("TILDE");
	IElementType QUEST = new IJavaElementType("QUEST");
	IElementType COLON = new IJavaElementType("COLON");
	IElementType PLUS = new IJavaElementType("PLUS");
	IElementType MINUS = new IJavaElementType("MINUS");
	IElementType ASTERISK = new IJavaElementType("ASTERISK");
	IElementType DIV = new IJavaElementType("DIV");
	IElementType AND = new IJavaElementType("AND");
	IElementType OR = new IJavaElementType("OR");
	IElementType XOR = new IJavaElementType("XOR");
	IElementType PERC = new IJavaElementType("PERC");

	IElementType EQEQ = new IJavaElementType("EQEQ");
	IElementType LE = new IJavaElementType("LE");
	IElementType GE = new IJavaElementType("GE");
	IElementType NE = new IJavaElementType("NE");
	IElementType ANDAND = new IJavaElementType("ANDAND");
	IElementType OROR = new IJavaElementType("OROR");
	IElementType PLUSPLUS = new IJavaElementType("PLUSPLUS");
	IElementType MINUSMINUS = new IJavaElementType("MINUSMINUS");
	IElementType LTLT = new IJavaElementType("LTLT");
	IElementType GTGT = new IJavaElementType("GTGT");
	IElementType GTGTGT = new IJavaElementType("GTGTGT");
	IElementType PLUSEQ = new IJavaElementType("PLUSEQ");
	IElementType MINUSEQ = new IJavaElementType("MINUSEQ");
	IElementType ASTERISKEQ = new IJavaElementType("ASTERISKEQ");
	IElementType DIVEQ = new IJavaElementType("DIVEQ");
	IElementType ANDEQ = new IJavaElementType("ANDEQ");
	IElementType OREQ = new IJavaElementType("OREQ");
	IElementType XOREQ = new IJavaElementType("XOREQ");
	IElementType PERCEQ = new IJavaElementType("PERCEQ");
	IElementType LTLTEQ = new IJavaElementType("LTLTEQ");
	IElementType GTGTEQ = new IJavaElementType("GTGTEQ");
	IElementType GTGTGTEQ = new IJavaElementType("GTGTGTEQ");

	TokenSet WHITE_SPACE_SET = TokenSet.create(WHITE_SPACE);
	TokenSet STRING_LITERAL_SET = TokenSet.create(STRING_LITERAL);
	TokenSet COMMENT_SET = TokenSet.create(C_STYLE_COMMENT, END_OF_LINE_COMMENT);
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
		SWITCH_KEYWORD, VOID_KEYWORD, WHILE_KEYWORD
	);
}
