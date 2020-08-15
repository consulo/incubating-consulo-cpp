package consulo.cpp.preprocessor.expand;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;
import consulo.cpp.preprocessor.psi.CPreprocessorDefineDirective;
import consulo.cpp.preprocessor.psi.CPreprocessorMacroReference;
import consulo.cpp.preprocessor.psi.CPsiSharpDefineValue;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorForeignLeafPsiElement;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorRecursiveElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @since 17:11/2020-07-31
 */
public class PreprocessorExpander {
	public static final Key<PreprocessorExpander> EXPANDER_KEY = Key.create("PreprocessorExpander");

	private final Map<String, ExpandedMacro> myDefines = new LinkedHashMap<>();

	private final CTemplateDataModifications myModifications = new CTemplateDataModifications();

	private final CPreprocessorDirectiveCollector rangeCollector = new CPreprocessorDirectiveCollector();

	public PreprocessorExpander(PsiFile preprocessorFile, ParserDefinition parserDefinition) {
		preprocessorFile.accept(new CPreprocessorRecursiveElementVisitor() {
			@Override
			public void visitSDefine(CPreprocessorDefineDirective element) {

				CPsiSharpDefineValue value = element.getValue();
				String text = element.getName();

				myModifications.addOuterRange(element.getTextRange());

				myDefines.put(text, new ExpandedMacro(parserDefinition, element, value.getNode().getChars()));
			}
		});
	}

	public CharSequence buildText(CharSequence sequence) {
		return rangeCollector.applyTemplateDataModifications(sequence, myModifications);
	}

	public CPreprocessorDirectiveCollector getRangeCollector() {
		return rangeCollector;
	}

	@Nullable
	public ExpandedMacro getMacro(@NotNull String name) {
		return myDefines.get(name);
	}

	@Nullable
	public ExpandedMacro tryToExpand(String name, int startOffset) {
		ExpandedMacro macro = myDefines.get(name);
		if (macro == null) {
			return null;
		}

		macro.expand(startOffset);
		return macro;
	}

	/**
	 * Insert zero-length element after macro reference for normalize psi tree
	 */
	public void insertDummyNodes(TreeElement parsed) {
		// we need insert dummy nodes before inserting outer elements, due it will change tree length
		// this dummy nodes not change tree text length
		int textLength = parsed.getTextLength();

		// call it for parse file tree
		parsed.getFirstChildNode();

		for (Map.Entry<String, ExpandedMacro> entry : myDefines.entrySet()) {
			String key = entry.getKey();
			ExpandedMacro value = entry.getValue();

			for (int expandedOffset : value.getExpandedOffsets()) {
				TextRange textRange = new TextRange(expandedOffset, expandedOffset + key.length());

				TreeElement node = (TreeElement) findNode(textRange, parsed, true);

				TreeElement next = null;
				for (Pair<IElementType, String> symbol : value.getSymbols()) {
					CPreprocessorForeignLeafPsiElement leaf = new CPreprocessorForeignLeafPsiElement(symbol.getFirst(), symbol.second);

					if (next == null) {
						TreeElement macroReference = (TreeElement) selectUpNodeIfMacroReference(node);

						macroReference.rawInsertAfterMe(next = leaf);
					} else {
						next.rawInsertAfterMe(leaf);
					}
				}
			}
		}
	}

	@NotNull
	private static ASTNode selectUpNodeIfMacroReference(ASTNode node) {
		if (node.getElementType() == CPsiTokens.IDENTIFIER) {
			ASTNode treeParent = node.getTreeParent();
			if (treeParent != null && treeParent.getPsi() instanceof CPreprocessorMacroReference) {
				return treeParent;
			}
		}

		return node;
	}

	private static ASTNode findNode(TextRange textRange, ASTNode parsed, boolean strict) {
		ASTNode node = parsed;

		while (node != null) {
			if (node instanceof LeafElement) {
				if (strict) {
					if (textRange.equals(node.getTextRange())) {
						return node;
					}
				} else {
					if (textRange.contains(node.getStartOffset())) {
						return node;
					}
				}
			} else {
				for (ASTNode child = node.getFirstChildNode(); child != null; child = child.getTreeNext()) {
					ASTNode result = findNode(textRange, child, strict);

					if (result != null) {
						return result;
					}
				}
			}

			node = node.getTreeNext();
		}

		return null;
	}
}
