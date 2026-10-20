package consulo.cpp.preprocessor.psi.stub;

import consulo.cpp.preprocessor.expand.CPreprocessorExpandContext;
import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Lightweight stub for a {@code #define} preprocessor directive.
 * <p>
 * The stub stores only the macro name and its replacement-text value so that
 * the {@link consulo.cpp.preprocessor.expand.PreprocessorExpander} can read
 * macro definitions from included files via the stub index without triggering
 * full PSI loading.
 *
 * @author VISTALL
 */
public interface CPreprocessorDefineStub extends StubElement<CPreprocessorDefineDirective>, CPreprocessorDirectiveStub {

    /** The macro name (the identifier after {@code #define}). */
    @Nullable
    String getName();

    /**
     * The raw replacement-text of the macro (everything after the name on the
     * {@code #define} line), or {@code null} / empty for object-like macros
     * with no replacement text.
     */
    @Nullable
    String getValueText();

    /**
     * See {@link CPreprocessorDefineDirective#getParameters()}.
     */
    @Nullable
    List<String> getParameters();

    @Override
    default void expand(CPreprocessorExpandContext context) {
        String name = getName();
        if (name != null) {
            String valueText = getValueText();
            context.define(name, getParameters(), valueText != null ? valueText : "", null);
        }
    }
}
