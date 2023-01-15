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
 * @since 07/12/2021
 */
public class CPPHeaderFileType extends LanguageFileType
{
	public static final CPPHeaderFileType INSTANCE = new CPPHeaderFileType();

	public CPPHeaderFileType()
	{
		super(CLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getId()
	{
		return "CPP_HEADER";
	}

	@NotNull
	@Override
	public LocalizeValue getDescription()
	{
		return LocalizeValue.localizeTODO("C++ header file");
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "hpp";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return CppApiIconGroup.filetypeHpp();
	}
}
