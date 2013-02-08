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

package org.napile.cpp4idea.config.facet.impl;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.config.facet.CBaseFacet;
import org.napile.cpp4idea.config.facet.CBaseFacetType;
import org.napile.cpp4idea.config.facet.CDialectProvider;
import org.napile.cpp4idea.lang.CDialect;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 13:49/08.02.13
 */
public class CDialectProviderImpl extends CDialectProvider
{
	@Override
	public CDialect findDialect(@NotNull VirtualFile file, @NotNull Project project)
	{
		Module module = ModuleUtil.findModuleForFile(file, project);
		if(module == null)
			return null;

		FacetManager facetManager = FacetManager.getInstance(module);

		CBaseFacet facet = facetManager.getFacetByType(CBaseFacetType.TYPE_ID);
		if(facet == null)
			return null;

		CDialect dialect = null;
		for(CDialect c : CDialect.EP_NAME.getExtensions())
			if(facet.getType().getClass() == c.getFacetType())
				dialect = c;
		return dialect;
	}

	@Override
	public CDialect findDialect(@NotNull PsiElement element)
	{
		Module module = ModuleUtil.findModuleForPsiElement(element);
		if(module == null)
			return null;

		FacetManager facetManager = FacetManager.getInstance(module);

		CBaseFacet facet = facetManager.getFacetByType(CBaseFacetType.TYPE_ID);
		if(facet == null)
			return null;

		CDialect dialect = null;
		for(CDialect c : CDialect.EP_NAME.getExtensions())
			if(facet.getType().getClass() == c.getFacetType())
				dialect = c;
		return dialect;
	}
}
