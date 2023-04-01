package consulo.cpp.lang.editor.highlight;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.preprocessor.psi.CPreprocessorMacroReference;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoHolder;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.language.editor.rawHighlight.HighlightVisitor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiEnumConstant;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psi.visitors.CPsiElementVisitor;

/**
 * @author VISTALL
 * @since 11:11/2020-08-01
 */
public class CHighlightVisitor extends CPsiElementVisitor implements HighlightVisitor
{
	private HighlightInfoHolder myHolder;

	@Override
	public void visit(@NotNull PsiElement element)
	{
		element.accept(this);
	}

	@Override
	public void visitPreprocessorMacroReference(CPreprocessorMacroReference reference)
	{
		PsiElement element = reference.resolve();

		PsiElement referenceElement = reference.getReferenceElement();
		if(element == null)
		{
			String errorText = "Not '" + referenceElement.getText() + "' resolved";
			myHolder.add(HighlightInfo.newHighlightInfo(HighlightInfoType.WRONG_REF).descriptionAndTooltip(errorText).range(referenceElement).create());
		}
		else
		{
			HighlightUtil.highlight(element, referenceElement, myHolder);
		}
	}

	@Override
	public void visitEnumConstant(CPsiEnumConstant element)
	{
		PsiElement nameElement = element.getNameIdentifier();
		if(nameElement != null)
		{
			HighlightUtil.highlight(element, nameElement, myHolder);
		}
	}

	@Override
	public boolean analyze(@NotNull PsiFile file, boolean updateWholeFile, @NotNull HighlightInfoHolder holder, @NotNull Runnable action)
	{
		myHolder = holder;
		action.run();
		return true;
	}
}
