package consulo.cpp.preprocessor.psi;

import consulo.cpp.preprocessor.psi.impl.*;
import consulo.language.ast.IElementType;
import consulo.language.impl.psi.ASTWrapperPsiElement;

/**
 * @author VISTALL
 * @since 2026-03-19
 */
public interface CPreprocessorElementTypes {
    IElementType DEFINE_VALUE =
        new CPreprocessorElementType(CPsiSharpDefineValue.class.getSimpleName(), CPsiSharpDefineValueImpl::new);

    IElementType DEFINE_DIRECTIVE = CPreprocessorDefineStubElementType.INSTANCE;

    /**
     * The parameters of a function-like macro, {@code (a, b, ...)}.
     */
    IElementType DEFINE_PARAMETER_LIST = new CPreprocessorElementType("DEFINE_PARAMETER_LIST", ASTWrapperPsiElement::new);

    /**
     * A macro use in the C text - see {@link consulo.cpp.preprocessor.expand.CMacroExpandingLexer}.
     */
    IElementType MACRO_INVOCATION = new CMacroInvocationElementType();

    IElementType MACRO_REFERENCE =
        new CPreprocessorElementType(CPreprocessorMacroReference.class.getSimpleName(), CPreprocessorMacroReferenceImpl::new);

    IElementType IF_BLOCK = CPreprocessorIfBlockStubElementType.INSTANCE;

    IElementType IF_BODY = CPreprocessorIfBodyStubElementType.INSTANCE;

    IElementType INCLUDE = CPreprocessorIncludeStubElementType.INSTANCE;

    IElementType INDEP_INCLUDE =
        new CPreprocessorElementType(CPsiSharpIndepInclude.class.getSimpleName(), CPsiSharpIndepIncludeImpl::new);

    IElementType UNDEF_DIRECTIVE = CPreprocessorUndefStubElementType.INSTANCE;

    IElementType PRAGMA_DIRECTIVE =
        new CPreprocessorElementType(CPreprocessorPragmaDirective.class.getSimpleName(), CPreprocessorPragmaDirectiveImpl::new);
}
