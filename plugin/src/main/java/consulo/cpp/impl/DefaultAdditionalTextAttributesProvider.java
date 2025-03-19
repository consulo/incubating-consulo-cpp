package consulo.cpp.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AttributesFlyweightBuilder;
import consulo.colorScheme.EditorColorSchemeExtender;
import consulo.colorScheme.EditorColorsScheme;
import consulo.ui.color.RGBColor;
import jakarta.annotation.Nonnull;
import org.napile.cpp4idea.ide.highlight.CSyntaxHighlighter;

/**
 * @author VISTALL
 * @since 15/01/2023
 */
@ExtensionImpl
public class DefaultAdditionalTextAttributesProvider implements EditorColorSchemeExtender {
    @Nonnull
    @Override
    public String getColorSchemeId() {
        return EditorColorsScheme.DEFAULT_SCHEME_NAME;
    }

    @Override
    public void extend(Builder builder) {
        builder.add(CSyntaxHighlighter.COMPILER_VARIABLE, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x5A, 0x02, 0xA5))
            .withBoldFont()
            .build());

        builder.add(CSyntaxHighlighter.LIGHT_COMPILER_VARIABLE, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0xA1, 0x92, 0xB4))
            .withBoldFont()
            .build());

        builder.add(CSyntaxHighlighter.LIGHT_KEYWORD, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x53, 0x81, 0xAC))
            .build());

        builder.add(CSyntaxHighlighter.LIGHT_STRING, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x73, 0xAB, 0x73))
            .build());

        builder.add(CSyntaxHighlighter.LIGHT_NUMBER, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x33, 0x66, 0xCC))
            .build());

        builder.add(CSyntaxHighlighter.LIGHT_IDENTIFIER, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0xA5, 0xA5, 0xA5))
            .build());
    }
}
