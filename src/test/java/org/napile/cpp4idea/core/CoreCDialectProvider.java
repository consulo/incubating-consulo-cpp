/*
 * Copyright 2010-2013 napile.org
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

package org.napile.cpp4idea.core;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.config.facet.CDialectProvider;
import org.napile.cpp4idea.lang.CDialect;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 13:55/08.02.13
 */
public class CoreCDialectProvider extends CDialectProvider
{
	private final CDialect targetDialect;

	public CoreCDialectProvider(CDialect targetDialect)
	{
		this.targetDialect = targetDialect;
	}

	@Override
	public CDialect findDialect(@NotNull VirtualFile file, @NotNull Project project)
	{
		return targetDialect;
	}

	@Override
	public CDialect findDialect(@NotNull PsiElement element)
	{
		return targetDialect;
	}
}
