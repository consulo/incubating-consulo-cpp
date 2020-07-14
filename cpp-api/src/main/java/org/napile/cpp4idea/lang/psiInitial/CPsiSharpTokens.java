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

import org.napile.cpp4idea.lang.psi.CTokenImpl;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 13:37/29.12.12
 */
public interface CPsiSharpTokens {
	// sharp keywords
	IElementType S_INCLUDE_KEYWORD = new CTokenImpl("S_INCLUDE_KEYWORD"); // #include
	IElementType S_DEFINE_KEYWORD = new CTokenImpl("S_DEFINE_KEYWORD"); // #define
	IElementType S_IFDEF_KEYWORD = new CTokenImpl("S_IFDEF_KEYWORD"); // #ifdef
	IElementType S_IFNDEF_KEYWORD = new CTokenImpl("S_IFNDEF_KEYWORD"); // #ifndef
	IElementType S_ENDIF_KEYWORD = new CTokenImpl("S_ENDIF_KEYWORD"); // #endif
	IElementType S_ELSE_KEYWORD = new CTokenImpl("S_ELSE_KEYWORD"); // #else
	IElementType S_PRAGMA_KEYWORD = new CTokenImpl("S_PRAGMA_KEYWORD"); // #pragma
}
