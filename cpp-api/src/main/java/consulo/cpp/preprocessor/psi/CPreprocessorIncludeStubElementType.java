package consulo.cpp.preprocessor.psi;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.impl.CPsiSharpIncludeImpl;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIncludeStub;
import consulo.cpp.preprocessor.psi.stub.impl.CPreprocessorIncludeStubImpl;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Stub element type for {@code #include "..."} preprocessor directives.
 * <p>
 * Stubbing the include path allows
 * {@link consulo.cpp.preprocessor.expand.PreprocessorExpander} to follow
 * <em>transitive</em> header chains (headers that themselves include other
 * headers) via the stub index, without triggering a full PSI load of any file.
 *
 * @author VISTALL
 */
public class CPreprocessorIncludeStubElementType
        extends IStubElementType<CPreprocessorIncludeStub, CPsiSharpInclude> {

    public static final CPreprocessorIncludeStubElementType INSTANCE = new CPreprocessorIncludeStubElementType();

    private CPreprocessorIncludeStubElementType() {
        super(CPsiSharpInclude.class.getSimpleName(), CPreprocessorLanguage.INSTANCE);
    }

    // -----------------------------------------------------------------------
    // AST → PSI  (called by CPreprocessorParserDefinition.createElement)
    // -----------------------------------------------------------------------

    public PsiElement createPsiFromNode(@NotNull ASTNode node) {
        return new CPsiSharpIncludeImpl(node);
    }

    // -----------------------------------------------------------------------
    // Stub → PSI
    // -----------------------------------------------------------------------

    @Override
    public @NotNull CPsiSharpInclude createPsi(@NotNull CPreprocessorIncludeStub stub) {
        return new CPsiSharpIncludeImpl(stub, this);
    }

    // -----------------------------------------------------------------------
    // PSI → Stub  (called during indexing)
    // -----------------------------------------------------------------------

    @Override
    public @NotNull CPreprocessorIncludeStub createStub(@NotNull CPsiSharpInclude psi,
                                                        StubElement parentStub) {
        return new CPreprocessorIncludeStubImpl(parentStub, this, psi.getIncludeName());
    }

    // -----------------------------------------------------------------------
    // Serialization
    // -----------------------------------------------------------------------

    @Override
    public @NotNull String getExternalId() {
        return "cpp.preprocessor.include";
    }

    @Override
    public void serialize(@NotNull CPreprocessorIncludeStub stub,
                          @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getIncludePath());
    }

    @Override
    public @NotNull CPreprocessorIncludeStub deserialize(@NotNull StubInputStream dataStream,
                                                         StubElement parentStub) throws IOException {
        String path = dataStream.readNameString();
        return new CPreprocessorIncludeStubImpl(parentStub, this, path);
    }

    // -----------------------------------------------------------------------
    // Indexing
    // -----------------------------------------------------------------------

    @Override
    public void indexStub(@NotNull CPreprocessorIncludeStub stub, @NotNull IndexSink sink) {
        // No custom index entries needed for now
    }
}
