package consulo.cpp.lang;

import consulo.cpp.api.icon.CppApiIconGroup;
import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import org.napile.cpp4idea.CLanguage;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 18:15/2020-08-15
 */
public class CSourceFileType extends LanguageFileType
{
	public static final CSourceFileType INSTANCE = new CSourceFileType();

	public CSourceFileType()
	{
		super(CLanguage.INSTANCE);
	}

	@Nonnull
	@Override
	public String getId()
	{
		return "C_SOURCE";
	}

	@Nonnull
	@Override
	public LocalizeValue getDescription()
	{
		return LocalizeValue.localizeTODO("C source file");
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return "c";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return CppApiIconGroup.filetypeC();
	}
}
