package consulo.cpp.preprocessor.psi.stub.impl;

import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorDefineStub;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubBase;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 */
public class CPreprocessorDefineStubImpl extends StubBase<CPreprocessorDefineDirective> implements CPreprocessorDefineStub {

    private final String myName;
    private final String myValueText;
    private final List<String> myParameters;

    public CPreprocessorDefineStubImpl(StubElement<?> parent,
                                       IStubElementType<?, ?> elementType,
                                       @Nullable String name,
                                       @Nullable String valueText,
                                       @Nullable List<String> parameters) {
        super(parent, elementType);
        myName = name;
        myValueText = valueText;
        myParameters = parameters;
    }

    @Override
    @Nullable
    public String getName() {
        return myName;
    }

    @Override
    @Nullable
    public String getValueText() {
        return myValueText;
    }

    @Override
    @Nullable
    public List<String> getParameters() {
        return myParameters;
    }
}
