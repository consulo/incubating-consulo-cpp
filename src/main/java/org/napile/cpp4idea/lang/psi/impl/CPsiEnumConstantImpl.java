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

package org.napile.cpp4idea.lang.psi.impl;

import org.napile.cpp4idea.lang.lexer.CTokenType;
import org.napile.cpp4idea.lang.psi.CPsiCompilerVariableHolder;
import org.napile.cpp4idea.lang.psi.CPsiEnumConstant;
import org.napile.cpp4idea.lang.psi.CPsiLiteralExpression;
import org.napile.cpp4idea.lang.psi.visitors.CPsiVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 17:51/14.12.2011
 */
public class CPsiEnumConstantImpl extends CPsiElementBaseImpl implements CPsiEnumConstant
{
	public CPsiEnumConstantImpl(@org.jetbrains.annotations.NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(CPsiVisitor visitor, CPsiCompilerVariableHolder variableHolder)
	{
		visitor.visitEnumConstant(this, variableHolder);
	}

	@Override
	public PsiElement getNameElement()
	{
		return findChildByType(CTokenType.IDENTIFIER);
	}

	@Override
	public CPsiLiteralExpression getConstantExpression()
	{
		return findChildByClass(CPsiLiteralExpression.class);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof CPsiEnumConstant))
			return false;

		PsiElement nameElement = getNameElement();
		if(nameElement == null)
			return false;
		PsiElement nameElement2 = ((CPsiEnumConstant) o).getNameElement();
		if(nameElement2 == null)
			return false;
		return nameElement.equals(nameElement2) ;
	}
}
