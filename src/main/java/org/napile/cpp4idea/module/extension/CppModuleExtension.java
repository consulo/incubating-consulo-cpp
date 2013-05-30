package org.napile.cpp4idea.module.extension;

import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.config.sdk.CSdkType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;

/**
 * @author VISTALL
 * @since 14:41/30.05.13
 */
public class CppModuleExtension extends ModuleExtensionWithSdkImpl<CppModuleExtension>
{
	public CppModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
	{
		return CSdkType.class;
	}
}
