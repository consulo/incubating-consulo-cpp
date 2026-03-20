package consulo.cpp.preprocessor.psi;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorDefineDirectiveImpl;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorDefineStub;
import consulo.cpp.preprocessor.psi.stub.impl.CPreprocessorDefineStubImpl;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Stub element type for {@code #define} preprocessor directives.
 * <p>
 * Registered as {@link CPreprocessorElementTypes#DEFINE_DIRECTIVE}.  The stub
 * stores the macro name and its raw replacement text so that
 * {@link consulo.cpp.preprocessor.expand.PreprocessorExpander} can read macro
 * definitions from included files through the stub index without triggering a
 * full PSI load of those files.
 *
 * @author VISTALL
 */
public class CPreprocessorDefineStubElementType
        extends IStubElementType<CPreprocessorDefineStub, CPreprocessorDefineDirective> {

    public static final CPreprocessorDefineStubElementType INSTANCE = new CPreprocessorDefineStubElementType();

    private CPreprocessorDefineStubElementType() {
        super(CPreprocessorDefineDirective.class.getSimpleName(), CPreprocessorLanguage.INSTANCE);
    }

    // -----------------------------------------------------------------------
    // AST → PSI  (called by CPreprocessorParserDefinition.createElement)
    // -----------------------------------------------------------------------

    /**
     * Factory used by {@link consulo.cpp.preprocessor.parser.CPreprocessorParserDefinition}
     * when the define element is represented as an AST node (not yet loaded from a stub).
     */
    public PsiElement createPsiFromNode(@NotNull ASTNode node) {
        return new CPreprocessorDefineDirectiveImpl(node);
    }

    // -----------------------------------------------------------------------
    // Stub → PSI
    // -----------------------------------------------------------------------

    @Override
    public @NotNull CPreprocessorDefineDirective createPsi(@NotNull CPreprocessorDefineStub stub) {
        return new CPreprocessorDefineDirectiveImpl(stub, this);
    }

    // -----------------------------------------------------------------------
    // PSI → Stub  (called during indexing)
    // -----------------------------------------------------------------------

    @Override
    public @NotNull CPreprocessorDefineStub createStub(@NotNull CPreprocessorDefineDirective psi,
                                                       StubElement parentStub) {
        String name = psi.getName();
        CPsiSharpDefineValue value = psi.getValue();
        String valueText = value != null ? value.getText() : null;
        return new CPreprocessorDefineStubImpl(parentStub, this, name, valueText);
    }

    // -----------------------------------------------------------------------
    // Serialization
    // -----------------------------------------------------------------------

    @Override
    public @NotNull String getExternalId() {
        return "cpp.preprocessor.define";
    }

    @Override
    public void serialize(@NotNull CPreprocessorDefineStub stub,
                          @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        dataStream.writeUTFFast(stub.getValueText() != null ? stub.getValueText() : "");
    }

    @Override
    public @NotNull CPreprocessorDefineStub deserialize(@NotNull StubInputStream dataStream,
                                                        StubElement parentStub) throws IOException {
        String name = dataStream.readNameString();
        String valueText = dataStream.readUTFFast();
        return new CPreprocessorDefineStubImpl(parentStub, this,
                name,
                valueText.isEmpty() ? null : valueText);
    }

    // -----------------------------------------------------------------------
    // Indexing
    // -----------------------------------------------------------------------

    @Override
    public void indexStub(@NotNull CPreprocessorDefineStub stub, @NotNull IndexSink sink) {
        // No custom index entries needed for now
    }
}
