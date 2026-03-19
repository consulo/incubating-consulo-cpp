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

    IElementType DEFINE_DIRECTIVE =
        new CPreprocessorElementType(CPreprocessorDefineDirective.class.getSimpleName(), CPreprocessorDefineDirectiveImpl::new);

    IElementType MACRO_REFERENCE =
        new CPreprocessorElementType(CPreprocessorMacroReference.class.getSimpleName(), CPreprocessorMacroReferenceImpl::new);

    IElementType IF_BLOCK =
        new CPreprocessorElementType(CPreprocessorIfBlock.class.getSimpleName(), CPreprocessorIfBlockImpl::new);

    IElementType IF_BODY =
        new CPreprocessorElementType(CPsiSharpIfBody.class.getSimpleName(), CPsiSharpIfBodyImpl::new);

    IElementType INCLUDE =
        new CPreprocessorElementType(CPsiSharpInclude.class.getSimpleName(), CPsiSharpIncludeImpl::new);

    IElementType INDEP_INCLUDE =
        new CPreprocessorElementType(CPsiSharpIndepInclude.class.getSimpleName(), CPsiSharpIndepIncludeImpl::new);

    IElementType UNDEF_DIRECTIVE =
        new CPreprocessorElementType(CPreprocessorUndefDirective.class.getSimpleName(), CPreprocessorUndefDirectiveImpl::new);

    IElementType PRAGMA_DIRECTIVE =
        new CPreprocessorElementType(CPreprocessorPragmaDirective.class.getSimpleName(), CPreprocessorPragmaDirectiveImpl::new);
}
