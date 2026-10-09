package consulo.cpp.preprocessor.psi;

import consulo.cpp.preprocessor.psi.impl.*;
import consulo.language.ast.IElementType;

/**
 * @author VISTALL
 * @since 2026-03-19
 */
public interface CPreprocessorElementTypes {
    IElementType DEFINE_VALUE =
        new CPreprocessorElementType(CPsiSharpDefineValue.class.getSimpleName(), CPsiSharpDefineValueImpl::new);

    IElementType DEFINE_DIRECTIVE = CPreprocessorDefineStubElementType.INSTANCE;

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
