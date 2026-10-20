package consulo.cpp.preprocessor.expand;

import consulo.cpp.lang.psi.CPsiTokens;
import consulo.cpp.lang.version.CLanguageVersion;
import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.language.ast.IElementType;
import consulo.language.lexer.Lexer;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A macro in scope: its name, the parameters of a function-like one, and its replacement text, which is lexed into
 * {@link #getBody() tokens} on first use. Whitespace and comments are no part of the body - only the tokens the
 * parser will see.
 *
 * @author VISTALL
 * @since 21:52/2020-07-31
 */
public class ExpandedMacro {
    private final CLanguageVersion myLanguageVersion;
    private final String myName;
    private final @Nullable List<String> myParameters;
    private final @Nullable CPreprocessorDefineDirective myElement;
    private final CharSequence myText;

    private @Nullable List<CMacroToken> myBody;

    public ExpandedMacro(CLanguageVersion languageVersion,
                         String name,
                         @Nullable List<String> parameters,
                         @Nullable CPreprocessorDefineDirective element,
                         CharSequence text) {
        myLanguageVersion = languageVersion;
        myName = name;
        myParameters = parameters;
        myElement = element;
        myText = text;
    }

    public String getName() {
        return myName;
    }

    /**
     * The parameters of a function-like macro, {@code null} for an object-like one.
     */
    public @Nullable List<String> getParameters() {
        return myParameters;
    }

    public boolean isFunctionLike() {
        return myParameters != null;
    }

    public CharSequence getText() {
        return myText;
    }

    public @Nullable CPreprocessorDefineDirective getElement() {
        return myElement;
    }

    public List<CMacroToken> getBody() {
        List<CMacroToken> body = myBody;
        if (body == null) {
            myBody = body = Collections.unmodifiableList(lex(myLanguageVersion, myText));
        }
        return body;
    }

    /**
     * The significant tokens of {@code text}: no whitespace, no comments, no line continuations.
     */
    public static List<CMacroToken> lex(CLanguageVersion languageVersion, CharSequence text) {
        List<CMacroToken> tokens = new ArrayList<>();
        Lexer lexer = languageVersion.createLexer();
        lexer.start(text);

        IElementType type;
        while ((type = lexer.getTokenType()) != null) {
            if (!languageVersion.getWhitespaceTokens().contains(type)
                && !languageVersion.getCommentTokens().contains(type)
                && type != CPsiTokens.NEXT_LINE) {
                tokens.add(new CMacroToken(type, lexer.getTokenText()));
            }
            lexer.advance();
        }
        return tokens;
    }
}
