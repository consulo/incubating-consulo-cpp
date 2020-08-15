package consulo.cpp.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.CLanguage;
import org.napile.cpp4idea.util.CIcons;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 18:15/2020-08-15
 */
public class CSourceFileType extends LanguageFileType {
	public static final CSourceFileType INSTANCE = new CSourceFileType();

	public CSourceFileType() {
		super(CLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "C_Source";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "C/C++ source file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "c";
	}

	@Override
	public Icon getIcon() {
		return CIcons.SourceFile;
	}
}
