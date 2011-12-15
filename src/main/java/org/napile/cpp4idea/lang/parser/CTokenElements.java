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

package org.napile.cpp4idea.lang.parser;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.lexer.CPsiTokenImpl;
import org.napile.cpp4idea.lang.psi.CPsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 11:27/14.12.2011
 */
public class CTokenElements
{
	private static final CTokenElements INSTANCE = new CTokenElements();

	private final Map<Class<? extends CPsiElement>, IElementType> _cache = new HashMap<Class<? extends CPsiElement>, IElementType>();

	@NotNull
	public static IElementType element(Class<? extends CPsiElement> clazz)
	{
		return INSTANCE.element0(clazz);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	private IElementType element0(Class<? extends CPsiElement> clazz)
	{
		try
		{
			Class<? extends CPsiElement> implClass = (Class<? extends CPsiElement>) Class.forName("org.napile.cpp4idea.lang.psi.impl." + clazz.getSimpleName() + "Impl");

			IElementType elementType = _cache.get(implClass);
			if(elementType == null)
				_cache.put(clazz, elementType = new CPsiTokenImpl(clazz.getSimpleName(), implClass));
			return elementType;
		}
		catch(ClassNotFoundException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}
