package consulo.cpp.lang.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.lang.psi.CPsiDeclaration;
import org.napile.cpp4idea.lang.psi.CPsiFile;

/**
 * @author VISTALL
 * @since 16:37/2020-07-31
 */
public class CFileImpl extends PsiFileBase implements CPsiFile {
	public CFileImpl(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, CLanguage.INSTANCE);
	}

	@Override
	public @NotNull FileType getFileType() {
		return getViewProvider().getFileType();
	}

	@Override
	public @NotNull CPsiDeclaration[] getDeclarations() {
		return findChildrenByClass(CPsiDeclaration.class);
	}
}
