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

package org.napile.cpp4idea;

import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.awt.TargetAWT;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.util.CIcons;

/**
 * @author VISTALL
 * @date 1:01/10.12.2011
 */
public class CFileType extends LanguageFileType
{
	public static final LanguageFileType INSTANCE = new CFileType();

	public CFileType()
	{
		super(CLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName()
	{
		return "C/C++";
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return "C/C++ file";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "c";
	}

	@Override
	public Image getIcon()
	{
		return TargetAWT.from(CIcons.SOURCE_FILE);
	}
}
