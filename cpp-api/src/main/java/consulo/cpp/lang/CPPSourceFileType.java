package consulo.cpp.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.cpp.api.icon.CppApiIconGroup;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import org.napile.cpp4idea.CLanguage;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 18:15/2020-08-15
 */
public class CPPSourceFileType extends LanguageFileType
{
	public static final CPPSourceFileType INSTANCE = new CPPSourceFileType();

	public CPPSourceFileType()
	{
		super(CLanguage.INSTANCE);
	}

	@Nonnull
	@Override
	public String getId()
	{
		return "CPP_SOURCE";
	}

	@Nonnull
	@Override
	public LocalizeValue getDescription()
	{
		return LocalizeValue.localizeTODO("C++ source file");
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return "cpp";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return CppApiIconGroup.fileTypeCpp();
	}
}
