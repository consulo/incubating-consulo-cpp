package consulo.cpp.preprocessor.fileProvider;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiManager;
import com.intellij.util.containers.ContainerUtil;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;

import java.util.Set;

/**
 * @author VISTALL
 * @since 19:21/2020-07-15
 */
public class CFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider {
	CFileViewProvider(@NotNull PsiManager manager, @NotNull VirtualFile virtualFile, boolean eventSystemEnabled) {
		super(manager, virtualFile, eventSystemEnabled);
	}

	@Override
	@NotNull
	public Language getBaseLanguage() {
		return CLanguage.INSTANCE;
	}

	@Override
	@NotNull
	public Set<Language> getLanguages() {
		return ContainerUtil.newHashSet(CLanguage.INSTANCE, CPreprocessorLanguage.INSTANCE);
	}

	@Override
	public boolean hasLanguage(@NotNull Language language) {
		return language == CLanguage.INSTANCE || language == CPreprocessorLanguage.INSTANCE;
	}

	@Override
	@NotNull
	protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(@NotNull VirtualFile fileCopy) {
		return new CFileViewProvider(getManager(), fileCopy, false);
	}
}
