package org.napile.cpp4idea.module.extension;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.util.CIcons;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 14:44/30.05.13
 */
public class CppModuleExtensionProvider implements ModuleExtensionProvider<CppModuleExtension, CppMutableModuleExtension>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return CIcons.SOURCE_FILE;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "C/C++";
	}

	@NotNull
	@Override
	public CppModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new CppModuleExtension(s, module);
	}

	@NotNull
	@Override
	public CppMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module)
	{
		return new CppMutableModuleExtension(s, module);
	}
}
