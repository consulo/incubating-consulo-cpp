package consulo.cpp.preprocessor.psi;

import consulo.cpp.preprocessor.psi.impl.CPreprocessorDefineDirectiveImpl;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorIfBlockImpl;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorMacroReferenceImpl;
import consulo.cpp.preprocessor.psi.impl.CPsiSharpDefineValueImpl;
import consulo.cpp.preprocessor.psi.impl.CPsiSharpIfBodyImpl;
import consulo.cpp.preprocessor.psi.impl.CPsiSharpIncludeImpl;
import consulo.cpp.preprocessor.psi.impl.CPsiSharpIndepIncludeImpl;
import consulo.language.ast.IElementType;

/**
 * @author VISTALL
 * @since 2026-03-19
 */
public interface CPreprocessorElementTypes {
    IElementType DEFINE_VALUE =
        new CPsiSharpTokenImpl(CPsiSharpDefineValue.class.getSimpleName(), CPsiSharpDefineValueImpl::new);

    IElementType DEFINE_DIRECTIVE =
        new CPsiSharpTokenImpl(CPreprocessorDefineDirective.class.getSimpleName(), CPreprocessorDefineDirectiveImpl::new);

    IElementType MACRO_REFERENCE =
        new CPsiSharpTokenImpl(CPreprocessorMacroReference.class.getSimpleName(), CPreprocessorMacroReferenceImpl::new);

    IElementType IF_BLOCK =
        new CPsiSharpTokenImpl(CPreprocessorIfBlock.class.getSimpleName(), CPreprocessorIfBlockImpl::new);

    IElementType IF_BODY =
        new CPsiSharpTokenImpl(CPsiSharpIfBody.class.getSimpleName(), CPsiSharpIfBodyImpl::new);

    IElementType INCLUDE =
        new CPsiSharpTokenImpl(CPsiSharpInclude.class.getSimpleName(), CPsiSharpIncludeImpl::new);

    IElementType INDEP_INCLUDE =
        new CPsiSharpTokenImpl(CPsiSharpIndepInclude.class.getSimpleName(), CPsiSharpIndepIncludeImpl::new);
}
