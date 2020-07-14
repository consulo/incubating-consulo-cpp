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

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CBundle;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;

/**
 * @author VISTALL
 * @date 17:14/11.12.2011
 */
public class CColorSettingsPageImpl implements ColorSettingsPage {
	@NotNull
	@Override
	public String getDisplayName() {
		return "C/C++";
	}

	@Override
	public Icon getIcon() {
		return CIcons.SOURCE_FILE;
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors() {
		return new AttributesDescriptor[]
				{
						new AttributesDescriptor(CBundle.message("keyword"), CSyntaxHighlighter.KEYWORD),
						new AttributesDescriptor(CBundle.message("number"), CSyntaxHighlighter.NUMBER),
						new AttributesDescriptor(CBundle.message("string"), CSyntaxHighlighter.STRING),
						new AttributesDescriptor(CBundle.message("constant"), CSyntaxHighlighter.CONSTANT),
						new AttributesDescriptor(CBundle.message("compiler.variable"), CSyntaxHighlighter.COMPILER_VARIABLE),
						new AttributesDescriptor(CBundle.message("comma"), CSyntaxHighlighter.COMMA),
						new AttributesDescriptor(CBundle.message("dot"), CSyntaxHighlighter.DOT),
						new AttributesDescriptor(CBundle.message("line.comment"), CSyntaxHighlighter.LINE_COMMENT),
						new AttributesDescriptor(CBundle.message("block.comment"), CSyntaxHighlighter.BLOCK_COMMENT),
				};
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors() {
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter() {
		return new PlainSyntaxHighlighter();
	}

	@NotNull
	@Override
	public String getDemoText() {
		return "<blockcomment>/*\n" +
				" * This is block comment\n" +
				" * napile project\n" +
				"*/</blockcomment>\n" +
				"<linecomment>//This is line comment</linecomment>\n" +
				"<kw>#include</kw> <string>\"example.h\"</string>\n\n" +
				"<kw>#ifdef</kw> <compilervar>VARTEST</compilervar>\n" +
				"<kw>int</kw> testVar = <number>1</number>;\n" +
				"<kw>#endif</kw>\n\n" +
				"<kw>enum</kw> ExEnum\n" +
				"{\n" +
				"  <const>Constant0</const><comma>,</comma>\n" +
				"  <const>Constant1</const> = <number>1</number>\n" +
				"}\n";
	}

	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		Map<String, TextAttributesKey> map = new HashMap<String, TextAttributesKey>(1);
		map.put("compilervar", CSyntaxHighlighter.COMPILER_VARIABLE);
		map.put("string", CSyntaxHighlighter.STRING);
		map.put("kw", CSyntaxHighlighter.KEYWORD);
		map.put("number", CSyntaxHighlighter.NUMBER);
		map.put("const", CSyntaxHighlighter.CONSTANT);
		map.put("comma", CSyntaxHighlighter.COMMA);
		map.put("dot", CSyntaxHighlighter.DOT);
		map.put("linecomment", CSyntaxHighlighter.LINE_COMMENT);
		map.put("blockcomment", CSyntaxHighlighter.BLOCK_COMMENT);
		return map;
	}
}
