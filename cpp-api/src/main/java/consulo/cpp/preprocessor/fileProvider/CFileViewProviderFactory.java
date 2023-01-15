package consulo.cpp.preprocessor.fileProvider;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.file.FileViewProvider;
import consulo.language.file.LanguageFileViewProviderFactory;
import consulo.language.psi.PsiManager;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 19:24/2020-07-15
 */
@ExtensionImpl
public class CFileViewProviderFactory implements LanguageFileViewProviderFactory
{
	@Override
	@NotNull
	public FileViewProvider createFileViewProvider(@NotNull VirtualFile file, Language language, @NotNull PsiManager manager, boolean eventSystemEnabled)
	{
		return new CFileViewProvider(manager, file, eventSystemEnabled);
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CLanguage.INSTANCE;
	}
}
