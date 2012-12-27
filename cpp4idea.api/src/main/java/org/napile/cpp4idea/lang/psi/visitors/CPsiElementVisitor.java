package org.napile.cpp4idea.lang.psi.visitors;

import org.napile.cpp4idea.lang.psi.*;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 13:07/16.12.2011
 */
public class CPsiElementVisitor extends PsiElementVisitor
{
	public void visitCFile(CPsiFile file)
	{
		visitFile(file);
	}

	public void visitSDefine(CPsiSharpDefine element)
	{
		visitElement(element);
	}

	public void visitSIfDef(CPsiSharpIfDef element)
	{
		visitElement(element);
	}

	public void visitSIfNotDef(CPsiSharpIfNotDef element)
	{
		visitElement(element);
	}

	public void visitSInclude(CPsiInclude element)
	{
		visitElement(element);
	}

	public void visitSIndependInclude(CPsiIndependInclude element)
	{
		visitElement(element);
	}

	public void visitCompilerVariable(CPsiCompilerVariable element)
	{
		visitElement(element);
	}

	public void visitImplementingMethod(CPsiImplementingMethod element)
	{
		visitElement(element);
	}

	public void visitEnum(CPsiEnum element)
	{
		visitElement(element);
	}

	public void visitEnumConstant(CPsiEnumConstant element)
	{
		visitElement(element);
	}
}
