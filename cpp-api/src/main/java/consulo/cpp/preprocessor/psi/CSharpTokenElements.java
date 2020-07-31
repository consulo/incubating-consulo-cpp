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

package consulo.cpp.preprocessor.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @date 11:27/14.12.2011
 */
public class CSharpTokenElements {
	private static final CSharpTokenElements INSTANCE = new CSharpTokenElements();

	private final Map<Class<? extends PsiElement>, IElementType> _cache = new HashMap<>();

	@NotNull
	public static IElementType element(Class<? extends PsiElement> clazz) {
		return INSTANCE.element0(clazz);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	private IElementType element0(Class<? extends PsiElement> clazz) {
		try {
			Class<? extends PsiElement> implClass = (Class<? extends PsiElement>) Class.forName("consulo.cpp.preprocessor.psi.impl." + clazz.getSimpleName() + "Impl");

			IElementType elementType = _cache.get(implClass);
			if (elementType == null) {
				_cache.put(implClass, elementType = new CPsiSharpTokenImpl(clazz.getSimpleName(), implClass));
			}
			return elementType;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
