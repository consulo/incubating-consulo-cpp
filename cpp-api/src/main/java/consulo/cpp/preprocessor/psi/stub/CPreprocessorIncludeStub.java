package consulo.cpp.preprocessor.psi.stub;

import consulo.cpp.preprocessor.psi.CPsiSharpInclude;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.Nullable;

/**
 * Lightweight stub for a {@code #include "..."} preprocessor directive.
 * <p>
 * Storing the include path in the stub allows
 * {@link consulo.cpp.preprocessor.expand.PreprocessorExpander} to resolve
 * <em>transitive</em> includes (headers that themselves include other headers)
 * purely from the stub index, without triggering any PSI loading.
 *
 * @author VISTALL
 */
public interface CPreprocessorIncludeStub extends StubElement<CPsiSharpInclude> {

    /**
     * The raw include path as it appears between the quotes in the source,
     * e.g. {@code "utils.h"} becomes {@code utils.h}.
     * Returns {@code null} if the path could not be determined.
     */
    @Nullable
    String getIncludePath();
}
