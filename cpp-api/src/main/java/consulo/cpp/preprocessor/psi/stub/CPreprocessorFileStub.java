package consulo.cpp.preprocessor.psi.stub;

import consulo.cpp.preprocessor.psi.CPreprocessorFile;
import consulo.language.psi.stub.PsiFileStub;

/**
 * Stub root for a C/C++ preprocessor file.
 * <p>
 * Child stubs ({@link CPreprocessorDefineStub}) represent each {@code #define}
 * directive in the file.
 *
 * @author VISTALL
 */
public interface CPreprocessorFileStub extends PsiFileStub<CPreprocessorFile> {
}
