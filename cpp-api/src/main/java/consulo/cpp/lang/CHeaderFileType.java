package consulo.cpp.lang;

import consulo.cpp.api.icon.CppApiIconGroup;
import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 18:16/2020-08-15
 */
public class CHeaderFileType extends LanguageFileType
{
	public static final CHeaderFileType INSTANCE = new CHeaderFileType();

	public CHeaderFileType()
	{
		super(CLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getId()
	{
		return "C_HEADER";
	}

	@NotNull
	@Override
	public LocalizeValue getDescription()
	{
		return LocalizeValue.localizeTODO("C header file");
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "h";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return CppApiIconGroup.filetypeH();
	}
}
