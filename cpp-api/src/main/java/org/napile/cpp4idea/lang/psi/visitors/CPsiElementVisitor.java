package org.napile.cpp4idea.lang.psi.visitors;

import consulo.cpp.preprocessor.psi.CPreprocessorMacroReference;
import consulo.language.psi.PsiElementVisitor;
import org.napile.cpp4idea.lang.psi.*;

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

	public void visitModifierList(CPsiModifierList modifierList)
	{
		visitElement(modifierList);
	}

	public void visitPreprocessorMacroReference(CPreprocessorMacroReference reference)
	{
		visitElement(reference);
	}
}
