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
import org.napile.cpp4idea.CFileType;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;

/**
 * @author VISTALL
 * @date 17:14/11.12.2011
 */
public class CColorSettingsPageImpl implements ColorSettingsPage
{
	@NotNull
	@Override
	public String getDisplayName()
	{
		return "C/C++";
	}

	@Override
	public Icon getIcon()
	{
		return CIcons.SOURCE_FILE;
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors()
	{
		return new AttributesDescriptor[]
		{
			new AttributesDescriptor("Compiler variable", CSyntaxHighlighter.COMPILER_VARIABLE),
		};
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors()
	{
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter()
	{
		return SyntaxHighlighter.PROVIDER.create(CFileType.INSTANCE, null, null);
	}

	@NotNull
	@Override
	public String getDemoText()
	{
		return "#ifdef <compilervar>VARTEST</compilervar>";
	}

	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
	{
		Map<String, TextAttributesKey> map = new HashMap<String, TextAttributesKey>(1);
		map.put("compilervar", CSyntaxHighlighter.COMPILER_VARIABLE);
		return map;
	}
}
