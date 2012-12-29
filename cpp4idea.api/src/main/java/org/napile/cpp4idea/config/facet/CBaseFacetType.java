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

package org.napile.cpp4idea.config.facet;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;

/**
 * @author VISTALL
 * @date 16:36/11.12.2011
 */
public abstract class CBaseFacetType extends FacetType<CBaseFacet, CBaseFacetConfiguration>
{
	public static final FacetTypeId<CBaseFacet> TYPE_ID = new FacetTypeId<CBaseFacet>("c");

	public CBaseFacetType(String strId, String presentableName)
	{
		super(TYPE_ID, strId, presentableName);
	}

	@Nullable
	public Icon getIcon()
	{
		return CIcons.SOURCE_FILE;
	}

	@Override
	public CBaseFacetConfiguration createDefaultConfiguration()
	{
		return new CBaseFacetConfiguration();
	}

	@Override
	public CBaseFacet createFacet(@NotNull Module module, String name, @NotNull CBaseFacetConfiguration configuration, @Nullable Facet underlyingFacet)
	{
		return new CBaseFacet(this, module, name, configuration, underlyingFacet);
	}

	@Override
	public boolean isSuitableModuleType(ModuleType moduleType)
	{
		return true;
	}
}
