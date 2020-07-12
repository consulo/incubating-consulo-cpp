package org.napile.cpp4idea.module.extension;

import com.intellij.openapi.projectRoots.SdkType;
import consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import consulo.roots.ModuleRootLayer;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.config.sdk.CSdkType;

/**
 * @author VISTALL
 * @since 14:41/30.05.13
 */
public class CppModuleExtension extends ModuleExtensionWithSdkImpl<CppModuleExtension>
{
	public CppModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return CSdkType.class;
	}
}
