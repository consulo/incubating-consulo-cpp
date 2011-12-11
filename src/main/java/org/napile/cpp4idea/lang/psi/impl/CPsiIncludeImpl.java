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

package org.napile.cpp4idea.lang.psi.impl;

import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.psi.CPsiInclude;
import com.intellij.lang.ASTNode;

/**
 * @author VISTALL
 * @date 1:53/11.12.2011
 */
public class CPsiIncludeImpl extends CPsiElementBaseImpl implements CPsiInclude
{
	public CPsiIncludeImpl(@org.jetbrains.annotations.NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public boolean isIndependentPath()
	{
		return findChildByType(CTokenType.STRING_INCLUDE_LITERAL) != null;
	}

	@Override
	public String getIncludeName()
	{
		return null;
	}
}
