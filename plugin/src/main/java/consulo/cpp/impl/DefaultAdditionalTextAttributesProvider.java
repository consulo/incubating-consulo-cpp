package consulo.cpp.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AdditionalTextAttributesProvider;
import consulo.colorScheme.EditorColorsScheme;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 15/01/2023
 */
@ExtensionImpl
public class DefaultAdditionalTextAttributesProvider implements AdditionalTextAttributesProvider
{
	@Nonnull
	@Override
	public String getColorSchemeName()
	{
		return EditorColorsScheme.DEFAULT_SCHEME_NAME;
	}

	@Nonnull
	@Override
	public String getColorSchemeFile()
	{
		return "/consulo/cpp/impl/Default.xml";
	}
}
