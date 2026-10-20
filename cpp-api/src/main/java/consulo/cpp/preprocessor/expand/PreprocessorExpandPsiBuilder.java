package consulo.cpp.preprocessor.expand;

import consulo.cpp.preprocessor.psi.CPreprocessorElementTypes;
import consulo.language.ast.IElementType;
import consulo.language.impl.ast.TokenWrapper;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderAdapter;
import org.jspecify.annotations.Nullable;

/**
 * Presents the {@link CMacroExpandingLexer expanded} token stream to the C parser as plain tokens. An expanded token
 * reads as the token it stands for; a macro invocation is not seen at all, and is consumed together with the first
 * token after it - so it lands in the same node as the start of its expansion.
 *
 * @author VISTALL
 * @since 21:20/2020-07-31
 */
public class PreprocessorExpandPsiBuilder extends PsiBuilderAdapter {
    public PreprocessorExpandPsiBuilder(PsiBuilder delegate) {
        super(delegate);
    }

    @Override
    public @Nullable IElementType getTokenType() {
        IElementType type = super.getTokenType();
        if (type != CPreprocessorElementTypes.MACRO_INVOCATION) {
            return unwrap(type);
        }

        IElementType next = lookAhead(0);
        if (next == null) {
            // nothing but macros expanding to nothing up to the end: they go to the node being finished
            while (super.getTokenType() == CPreprocessorElementTypes.MACRO_INVOCATION) {
                super.advanceLexer();
            }
        }
        return next;
    }

    @Override
    public @Nullable String getTokenText() {
        int step = 0;
        IElementType type;
        while ((type = rawLookup(step)) != null && isSkipped(type)) {
            step++;
        }
        if (type == null) {
            return null;
        }
        if (type instanceof TokenWrapper wrapper) {
            return wrapper.getValue().toString();
        }
        return getOriginalText().subSequence(rawTokenTypeStart(step), rawTokenTypeStart(step + 1)).toString();
    }

    @Override
    public boolean eof() {
        return getTokenType() == null;
    }

    @Override
    public void advanceLexer() {
        while (super.getTokenType() == CPreprocessorElementTypes.MACRO_INVOCATION) {
            super.advanceLexer();
        }
        super.advanceLexer();
    }

    /**
     * The token {@code steps} ahead, macro invocations not counted.
     */
    @Override
    public @Nullable IElementType lookAhead(int steps) {
        int remaining = steps;
        for (int step = 0; ; step++) {
            IElementType type = super.lookAhead(step);
            if (type == null) {
                return null;
            }
            if (type == CPreprocessorElementTypes.MACRO_INVOCATION) {
                continue;
            }
            if (remaining-- == 0) {
                return unwrap(type);
            }
        }
    }

    private boolean isSkipped(IElementType type) {
        return type == CPreprocessorElementTypes.MACRO_INVOCATION || isWhitespaceOrCommentType(type);
    }

    private static @Nullable IElementType unwrap(@Nullable IElementType type) {
        return type instanceof TokenWrapper wrapper ? wrapper.getDelegate() : type;
    }
}
