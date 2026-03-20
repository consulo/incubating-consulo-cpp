package consulo.cpp.preprocessor.psi;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorFileStub;
import consulo.cpp.preprocessor.psi.stub.impl.CPreprocessorFileStubImpl;
import consulo.language.psi.stub.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Stub file element type for the C/C++ preprocessor language.
 * <p>
 * Replaces the plain {@code IFileElementType} previously used in
 * {@link consulo.cpp.preprocessor.parser.CPreprocessorParserDefinition}.
 * Enabling stubs for the preprocessor language allows
 * {@link consulo.cpp.preprocessor.expand.PreprocessorExpander} to read
 * {@code #define} macros from included files via the stub index without
 * triggering a full PSI load of those files.
 *
 * @author VISTALL
 */
public class CPreprocessorFileElementType extends IStubFileElementType<CPreprocessorFileStub> {

    public static final CPreprocessorFileElementType INSTANCE = new CPreprocessorFileElementType();

    /**
     * Bump this version whenever the stub serialization format changes so that
     * existing stubs on disk are invalidated and rebuilt.
     */
    private static final int STUB_VERSION = 1;

    private CPreprocessorFileElementType() {
        super("cpp.preprocessor.file", CPreprocessorLanguage.INSTANCE);
    }

    @Override
    public int getStubVersion() {
        return STUB_VERSION;
    }

    @Override
    public @NotNull String getExternalId() {
        return "cpp.preprocessor.file";
    }

    // -----------------------------------------------------------------------
    // Serialization — the file stub itself carries no data beyond its children
    // -----------------------------------------------------------------------

    @Override
    public void serialize(@NotNull CPreprocessorFileStub stub,
                          @NotNull StubOutputStream dataStream) throws IOException {
        // Nothing to write at the file level; child stubs are handled separately
    }

    @Override
    public @NotNull CPreprocessorFileStub deserialize(@NotNull StubInputStream dataStream,
                                                      StubElement parentStub) throws IOException {
        return new CPreprocessorFileStubImpl(null, this);
    }

    @Override
    public void indexStub(@NotNull CPreprocessorFileStub stub, @NotNull IndexSink sink) {
        // No per-file index entries needed at the file level
    }
}
