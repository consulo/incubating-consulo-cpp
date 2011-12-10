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

package org.napile.cpp4idea.lang.parser;

import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.psi.impl.CPsiMethodImpl;
import com.intellij.psi.impl.source.PsiParameterImpl;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 5:34/10.12.2011
 */
public interface CElementType extends CTokenType
{
	IElementType METHOD_ELEMENT = new CReflectElementType("METHOD_ELEMENT", CPsiMethodImpl.class);

	IElementType PARAMETER_LIST_ELEMENT = new CReflectElementType("PARAMETER_LIST_ELEMENT", PsiParameterImpl.class);
}
