package consulo.cpp.preprocessor.fileProvider;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorFileImpl;
import consulo.language.Language;
import consulo.language.impl.file.MultiplePsiFilesPerDocumentFileViewProvider;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.CLanguage;

import java.util.Set;

/**
 * @author VISTALL
 * @since 19:21/2020-07-15
 */
public class CFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider
{
	CFileViewProvider(@NotNull PsiManager manager, @NotNull VirtualFile virtualFile, boolean eventSystemEnabled)
	{
		super(manager, virtualFile, eventSystemEnabled);
	}

	@Override
	@NotNull
	public Language getBaseLanguage()
	{
		return CLanguage.INSTANCE;
	}

	@Override
	@NotNull
	public Set<Language> getLanguages()
	{
		return Set.of(CLanguage.INSTANCE, CPreprocessorLanguage.INSTANCE);
	}

	@Override
	public boolean hasLanguage(@NotNull Language language)
	{
		return language == CLanguage.INSTANCE || language == CPreprocessorLanguage.INSTANCE;
	}

	@Override
	protected
	@Nullable
	PsiFile createFile(@NotNull Language lang)
	{
		if(lang == CPreprocessorLanguage.INSTANCE)
		{
			return new CPreprocessorFileImpl(this);
		}
		return super.createFile(lang);
	}

	@Override
	@NotNull
	protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(@NotNull VirtualFile fileCopy)
	{
		return new CFileViewProvider(getManager(), fileCopy, false);
	}
}
