package consulo.cpp.preprocessor.expand;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.TreeElement;
import consulo.cpp.preprocessor.psi.CPsiSharpDefine;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorOuterLanguageElement;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorRecursiveElementVisitor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @since 17:11/2020-07-31
 */
public class PreprocessorExpander {
	private Map<PreprocessorAction, TextRange> myRanges = new LinkedHashMap<>();

	public PreprocessorExpander(PsiFile preprocessorFile) {
		preprocessorFile.accept(new CPreprocessorRecursiveElementVisitor() {
			@Override
			public void visitSDefine(CPsiSharpDefine element) {
				myRanges.put(new DefineAction(element.getNode().getChars()), element.getTextRange());
			}
		});
	}

	public StringBuilder buildText(CharSequence sequence) {
		StringBuilder builder = new StringBuilder();

		// todo this is slow

		loop:
		for (int i = 0; i < sequence.length(); i++) {
			// if element is preprocessor part - ignore
			for (TextRange value : myRanges.values()) {
				if (value.contains(i)) {
					continue loop;
				}
			}

			char c = sequence.charAt(i);

			builder.append(c);
		}

		return builder;
	}

	public void insert(ASTNode parsed) {
		for (Map.Entry<PreprocessorAction, TextRange> entry : myRanges.entrySet()) {
			PreprocessorAction key = entry.getKey();
			TextRange value = entry.getValue();

			TreeElement node = (TreeElement) findNode(value, parsed);



			node.rawInsertBeforeMe(new CPreprocessorOuterLanguageElement(key.getSequence()));

			System.out.println();
		}
	}

	private static ASTNode findNode(TextRange textRange, ASTNode parsed) {
		ASTNode node = parsed;

		while (node != null) {
			if(textRange.contains(node.getStartOffset())) {
				return node;
			}

			node = node.getTreeNext();
		}

		return null;
	}
}
