package consulo.cpp.preprocessor.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.CDialect;

/**
 * @author VISTALL
 * @since 20:32/2020-07-25
 */
public class CPreprocessorParser implements PsiParser {
	@Override
	public @NotNull ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
		builder.setDebugMode(true);
		return CDialect.parseInitial(builder, root);
	}
}
