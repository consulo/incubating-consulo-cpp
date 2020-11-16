package consulo.cpp.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.util.CIcons;

/**
 * @author VISTALL
 * @since 18:16/2020-08-15
 */
public class CHeaderFileFile extends LanguageFileType {
	public static final CHeaderFileFile INSTANCE = new CHeaderFileFile();

	public CHeaderFileFile() {
		super(CLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getId() {
		return "C_Header";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "C/C++ header file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "h";
	}

	@Override
	public Image getIcon() {
		return CIcons.HeaderFile;
	}
}
