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

package org.napile.cpp4idea.util;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariable;
import org.napile.cpp4idea.lang.psi.CPsiSharpVariableChecker;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 14:27/16.12.2011
 */
public class CPsiUtil
{
	public static boolean isBlockDefined(final @NotNull PsiElement element, Set<String> varSet)
	{
		PsiElement parent = element.getParent();
		if(parent == null)
			return true;

		if(parent instanceof CPsiSharpVariableChecker)
		{
			CPsiCompilerVariable variable = ((CPsiSharpVariableChecker) parent).getVariable();
			if(variable == null)
				return false;

			boolean eq = varSet.contains(variable.getText()) == ((CPsiSharpVariableChecker) parent).getEqualValue();

			if(!eq)
				return false;
		}

		return isBlockDefined(parent, varSet);
	}
}
