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

import org.napile.cpp4idea.lang.lexer.CPsiTokenImpl;
import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.psi.impl.*;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @date 5:34/10.12.2011
 */
public interface CElementType extends CTokenType
{
	// method with code block
	IElementType IMPLEMENTING_METHOD_ELEMENT = new CPsiTokenImpl("IMPLEMENTING_METHOD_ELEMENT", CPsiImplentingMethodImpl.class);

	// method without code block
	IElementType DECLARATION_METHOD_ELEMENT = new CPsiTokenImpl("DECLARATION_METHOD_ELEMENT", CPsiDeclarationMethodImpl.class);

	// field
	IElementType FIELD_ELEMENT = new CPsiTokenImpl("FIELD_ELEMENT", CPsiFieldImpl.class);

	// type define
	IElementType TYPE_REF_ELEMENT = new CPsiTokenImpl("TYPE_REF_ELEMENT", CPsiTypeRefImpl.class);

	// parameter list ()
	IElementType PARAMETER_LIST_ELEMENT = new CPsiTokenImpl("PARAMETER_LIST_ELEMENT", CPsiParameterListImpl.class);

	// parameter element
	IElementType PARAMETER_ELEMENT = new CPsiTokenImpl("PARAMETER_ELEMENT", CPsiParameterImpl.class);

	IElementType COMPILER_VARIABLE_ELEMENT = new CPsiTokenImpl("COMPILER_VARIABLE_ELEMENT", CPsiCompilerVariableImpl.class);

	IElementType CODE_BLOCK_ELEMENT = new CPsiTokenImpl("CODE_BLOCK_ELEMENT", CPsiCodeBlockImpl.class);

	// statement
	IElementType RETURN_ELEMENT = new CPsiTokenImpl("RETURN_ELEMENT", CPsiReturnStatementImpl.class);

	// #inculde
	IElementType INCLUDE_ELEMENT = new CPsiTokenImpl("INCLUDE_ELEMENT", CPsiIncludeImpl.class);

	// #define
	IElementType DEFINE_ELEMENT = new CPsiTokenImpl("DEFINE_ELEMENT", CPsiDefineImpl.class);

	// #ifdef
	IElementType IF_DEF_ELEMENT = new CPsiTokenImpl("IF_DEF_ELEMENT", CPsiIfDefHolderImpl.class);
	// #ifndef
	IElementType IF_NOT_DEF_ELEMENT = new CPsiTokenImpl("IF_NOT_DEF_ELEMENT", CPsiIfNotDefHolderImpl.class);

	// typedef
	IElementType TYPEDEF_ELEMENT = new CPsiTokenImpl("TYPEDEF_ELEMENT", CPsiTypeDeclarationImpl.class);

	// literal expression
	IElementType LITERAL_EXPRESSION_ELEMENT = new CPsiTokenImpl("LITERAL_EXPRESSION_ELEMENT", CPsiLiteralExpressionImpl.class);
	// (...) expression
	IElementType PARENTHESIZED_EXPRESSION_ELEMENT = new CPsiTokenImpl("PARENTHESIZED_EXPRESSION_ELEMENT", CPsiParenthesizedExpressionImpl.class);
	// +/-/*// - expression
	IElementType BINARY_EXPRESSION_ELEMENT = new CPsiTokenImpl("BINARY_EXPRESSION_ELEMENT", CPsiBinaryExpressionImpl.class);
}
