/*
 * Copyright 2010-2012 napile.org
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

package org.napile.cpp4idea.lang.psiInitial;

import java.lang.reflect.Constructor;

import org.napile.cpp4idea.lang.psi.CTokenImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 5:34/10.12.2011
 */
public class CPsiSharpTokenImpl extends CTokenImpl
{
	private Constructor<? extends PsiElement> _clazz;

	public CPsiSharpTokenImpl(@org.jetbrains.annotations.NonNls String debugName, Class<? extends PsiElement> clazz)
	{
		super(debugName);

		try
		{
			_clazz = clazz.getConstructor(ASTNode.class);
		}
		catch(NoSuchMethodException e)
		{
			//
		}
	}

	public PsiElement createPsi(ASTNode node)
	{
		PsiElement element = null;
		try
		{
			element = _clazz.newInstance(node);
		}
		catch(Exception e)
		{
			//
		}
		return element;
	}
}
