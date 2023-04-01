package consulo.cpp.lang.editor.highlight;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightVisitor;
import consulo.language.editor.rawHighlight.HighlightVisitorFactory;
import consulo.language.psi.PsiFile;
import org.napile.cpp4idea.lang.psi.CPsiFile;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 01/04/2023
 */
@ExtensionImpl
public class CHighlightVisitorFactory implements HighlightVisitorFactory
{
	@Override
	public boolean suitableForFile(@Nonnull PsiFile file)
	{
		return file instanceof CPsiFile;
	}

	@Nonnull
	@Override
	public HighlightVisitor createVisitor()
	{
		return new CHighlightVisitor();
	}
}
