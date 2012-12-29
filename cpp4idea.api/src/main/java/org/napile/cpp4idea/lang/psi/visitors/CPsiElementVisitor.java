package org.napile.cpp4idea.lang.psi.visitors;

import org.napile.cpp4idea.lang.psi.CPsiEnum;
import org.napile.cpp4idea.lang.psi.CPsiEnumConstant;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psi.CPsiImplementingMethod;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @date 13:07/16.12.2011
 */
public class CPsiElementVisitor extends PsiElementVisitor
{
	public void visitCFile(CPsiFile file)
	{
		visitElement(file);
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
