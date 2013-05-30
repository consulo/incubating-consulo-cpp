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

package org.napile.cpp4idea.ide.highlight;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.lexer._CppLexer;
import org.napile.cpp4idea.lang.psi.CPsiTokens;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @date 5:06/10.12.2011
 */
public class CSyntaxHighlighter extends SyntaxHighlighterBase
{
	public static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("C.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey LIGHT_KEYWORD = TextAttributesKey.createTextAttributesKey("C.LIGHT_KEYWORD");

	public static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("C.STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey LIGHT_STRING = TextAttributesKey.createTextAttributesKey("C.LIGHT_STRING");

	public static final TextAttributesKey COMPILER_VARIABLE = TextAttributesKey.createTextAttributesKey("C.COMPILER_VARIABLE");
	public static final TextAttributesKey LIGHT_COMPILER_VARIABLE = TextAttributesKey.createTextAttributesKey("C.LIGHT_COMPILER_VARIABLE");

	public static final TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey("C.NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey LIGHT_NUMBER = TextAttributesKey.createTextAttributesKey("C.LIGHT_NUMBER");

	public static final TextAttributesKey LIGHT_IDENTIFIER = TextAttributesKey.createTextAttributesKey("C.LIGHT_IDENTIFIER");

	public static final TextAttributesKey CONSTANT = TextAttributesKey.createTextAttributesKey("C.CONSTANT", DefaultLanguageHighlighterColors.STATIC_FIELD);

	public static final TextAttributesKey COMMA = TextAttributesKey.createTextAttributesKey("C.COMMA", DefaultLanguageHighlighterColors.COMMA);
	public static final TextAttributesKey DOT = TextAttributesKey.createTextAttributesKey("C.DOT", DefaultLanguageHighlighterColors.DOT);
	public static final TextAttributesKey LINE_COMMENT = TextAttributesKey.createTextAttributesKey("C.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("C.BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	public static final Map<IElementType, TextAttributesKey[]> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey[]>();

	static
	{
		fillMap(CPsiTokens.END_OF_LINE_COMMENT, LINE_COMMENT, LINE_COMMENT);
		fillMap(CPsiTokens.C_STYLE_COMMENT, BLOCK_COMMENT, BLOCK_COMMENT);
		fillMap(CPsiTokens.KEYWORD_SET, KEYWORD, LIGHT_KEYWORD);
		fillMap(CPsiTokens.STRING_LITERAL_SET, STRING, LIGHT_STRING);
		fillMap(CPsiTokens.STRING_INCLUDE_LITERAL, STRING, LIGHT_STRING);
		// numbers
		fillMap(CPsiTokens.INTEGER_LITERAL, NUMBER, LIGHT_NUMBER);
		fillMap(CPsiTokens.LONG_LITERAL, NUMBER, LIGHT_NUMBER);
		fillMap(CPsiTokens.FLOAT_LITERAL, NUMBER, LIGHT_NUMBER);
		fillMap(CPsiTokens.DOUBLE_LITERAL, NUMBER, LIGHT_NUMBER);
		fillMap(CPsiTokens.BOOL_LITERAL, KEYWORD, LIGHT_KEYWORD);
	}

	private static void fillMap(IElementType elementType, TextAttributesKey... keys)
	{
		ATTRIBUTES.put(elementType, keys);
	}

	private static void fillMap(TokenSet tokenSet, TextAttributesKey... keys)
	{
		for(IElementType elementType : tokenSet.getTypes())
			ATTRIBUTES.put(elementType, keys);
	}

	private final Project project;
	private final VirtualFile virtualFile;

	public CSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile)
	{
		this.project = project;
		this.virtualFile = virtualFile;
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new FlexAdapter(new _CppLexer());
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		TextAttributesKey[] keys = ATTRIBUTES.get(tokenType);
		return pack(keys == null ? null : keys[0]);
	}

	public TextAttributesKey[] getAttributes(IElementType e)
	{
		return ATTRIBUTES.get(e);
	}
}
