package consulo.cpp.preprocessor.psi.stub.impl;

import consulo.cpp.preprocessor.psi.CPreprocessorFile;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorFileStub;
import consulo.language.psi.stub.IStubFileElementType;
import consulo.language.psi.stub.PsiFileStubImpl;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 */
public class CPreprocessorFileStubImpl extends PsiFileStubImpl<CPreprocessorFile> implements CPreprocessorFileStub {

    private final IStubFileElementType<?> myType;

    public CPreprocessorFileStubImpl(@Nullable CPreprocessorFile file, IStubFileElementType<?> type) {
        super(file);
        myType = type;
    }

    @Override
    public IStubFileElementType<?> getType() {
        return myType;
    }
}
