package consulo.cpp.preprocessor.fileProvider;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 19:24/2020-07-15
 */
public class CFileViewProviderFactory implements FileViewProviderFactory {
	@Override
	@NotNull
	public FileViewProvider createFileViewProvider(@NotNull VirtualFile file, Language language, @NotNull PsiManager manager, boolean eventSystemEnabled) {
		return new CFileViewProvider(manager, file, eventSystemEnabled);
	}
}
