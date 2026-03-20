package consulo.cpp.preprocessor.psi.stub.impl;

import consulo.cpp.preprocessor.psi.CPsiSharpInclude;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIncludeStub;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubBase;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 */
public class CPreprocessorIncludeStubImpl extends StubBase<CPsiSharpInclude> implements CPreprocessorIncludeStub {

    private final String myIncludePath;

    public CPreprocessorIncludeStubImpl(StubElement<?> parent,
                                        IStubElementType<?, ?> elementType,
                                        @Nullable String includePath) {
        super(parent, elementType);
        myIncludePath = includePath;
    }

    @Override
    @Nullable
    public String getIncludePath() {
        return myIncludePath;
    }
}
