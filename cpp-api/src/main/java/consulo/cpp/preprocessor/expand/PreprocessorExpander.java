package consulo.cpp.preprocessor.expand;

import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.*;
import consulo.cpp.preprocessor.psi.impl.CPreprocessorForeignLeafPsiElement;
import consulo.cpp.preprocessor.psi.impl.visitor.CPreprocessorRecursiveElementVisitor;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorDefineStub;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorFileStub;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIncludeStub;
import consulo.language.psi.stub.StubElement;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBodyStub;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBlockStub;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorUndefStub;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.language.impl.ast.CompositeElement;
import consulo.language.impl.ast.LeafElement;
import consulo.language.impl.ast.TreeElement;
import consulo.language.parser.ParserDefinition;
import consulo.language.psi.PsiFile;
import consulo.language.psi.stub.ObjectStubTree;
import consulo.language.psi.stub.Stub;
import consulo.language.psi.stub.StubTreeLoader;
import consulo.project.Project;
import consulo.util.dataholder.Key;
import consulo.util.lang.Couple;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author VISTALL
 * @since 17:11/2020-07-31
 */
public class PreprocessorExpander {
    public static final Key<PreprocessorExpander> EXPANDER_KEY = Key.create("PreprocessorExpander");

    private final Map<String, ExpandedMacro> myDefines = new LinkedHashMap<>();

    private final CTemplateDataModifications myModifications = new CTemplateDataModifications();

    private final CPreprocessorDirectiveCollector rangeCollector = new CPreprocessorDirectiveCollector();

    private final List<TextRange> myInactiveRanges = new ArrayList<>();

    private final PsiFile myPreprocessorFile;
    private final ParserDefinition myParserDefinition;
    private final Set<VirtualFile> myProcessedFiles;
    /**
     * Configured include search directories (analogous to {@code -I} flags).
     * Used as a fallback for {@code #include "..."} when the path is not found
     * relative to the current file, and as the primary source for
     * {@code #include <...>}.
     */
    private final List<VirtualFile> myIncludePaths;

    public PreprocessorExpander(PsiFile preprocessorFile, ParserDefinition parserDefinition) {
        this(preprocessorFile, parserDefinition, new HashSet<>(), Collections.emptyList(), Collections.emptyMap());
    }

    public PreprocessorExpander(PsiFile preprocessorFile,
                                ParserDefinition parserDefinition,
                                @NotNull List<VirtualFile> includePaths) {
        this(preprocessorFile, parserDefinition, new HashSet<>(), includePaths, Collections.emptyMap());
    }

    /**
     * Expands with {@code seedDefines} already in scope, as if every name had been defined above the first line.
     * This is how per-module defines and the defines of an including file reach the expansion.
     */
    public PreprocessorExpander(PsiFile preprocessorFile,
                                ParserDefinition parserDefinition,
                                @NotNull List<VirtualFile> includePaths,
                                @NotNull Map<String, String> seedDefines) {
        this(preprocessorFile, parserDefinition, new HashSet<>(), includePaths, seedDefines);
    }

    private PreprocessorExpander(PsiFile preprocessorFile,
                                 ParserDefinition parserDefinition,
                                 Set<VirtualFile> processedFiles,
                                 List<VirtualFile> includePaths,
                                 Map<String, String> seedDefines) {
        myPreprocessorFile = preprocessorFile;
        myParserDefinition = parserDefinition;
        myProcessedFiles = processedFiles;
        myIncludePaths = includePaths;

        for (Map.Entry<String, String> seed : seedDefines.entrySet()) {
            myDefines.put(seed.getKey(), new ExpandedMacro(parserDefinition, null, seed.getValue()));
        }

        VirtualFile vFile = preprocessorFile.getVirtualFile();
        if (vFile != null) {
            myProcessedFiles.add(vFile);
        }

        preprocessorFile.accept(new CPreprocessorRecursiveElementVisitor() {
            @Override
            public void visitSDefine(CPreprocessorDefineDirective element) {
                CPsiSharpDefineValue value = element.getValue();
                String text = element.getName();

                myModifications.addOuterRange(element.getTextRange());

                CharSequence macroText = value == null ? "" : value.getNode().getChars();
                myDefines.put(text, new ExpandedMacro(parserDefinition, element, macroText));
            }

            @Override
            public void visitSUndef(consulo.cpp.preprocessor.psi.CPreprocessorUndefDirective element) {
                myModifications.addOuterRange(element.getTextRange());

                // remove the macro from the active defines so subsequent references are not expanded
                String name = element.getName();
                if (name != null) {
                    myDefines.remove(name);
                }
            }

            @Override
            public void visitSPragma(consulo.cpp.preprocessor.psi.CPreprocessorPragmaDirective element) {
                // #pragma directives have no effect on the C/C++ parse; remove from output
                myModifications.addOuterRange(element.getTextRange());
            }

            @Override
            public void visitPreprocessorIfBlock(CPreprocessorIfBlock element) {
                TextRange blockRange = element.getTextRange();
                CPsiSharpIfBody activeBody = selectActiveBody(element);

                if (activeBody == null) {
                    myModifications.addOuterRange(blockRange);
                }
                else {
                    TextRange activeRange = activeBody.getTextRange();
                    addOuterRange(blockRange.getStartOffset(), activeRange.getStartOffset());
                    activeBody.acceptChildren(this);
                    addOuterRange(activeRange.getEndOffset(), blockRange.getEndOffset());
                }

                for (PsiElement branch = element.getFirstChild(); branch != null; branch = branch.getNextSibling()) {
                    if (branch instanceof CPsiSharpIfBody && branch != activeBody) {
                        myInactiveRanges.add(branch.getTextRange());
                    }
                }
            }

            @Override
            public void visitSInclude(CPsiSharpInclude element) {
                myModifications.addOuterRange(element.getTextRange());
                // Resolve local #include "..." — current file's dir first, then include paths
                processInclude(element.getIncludeName());
            }

            @Override
            public void visitSIndependInclude(CPsiSharpIndepInclude element) {
                myModifications.addOuterRange(element.getTextRange());
                // System #include <...> — search configured include paths only
                processIndependInclude(element.getIncludeName());
            }
        });
    }

    // -----------------------------------------------------------------------
    // Include resolution
    // -----------------------------------------------------------------------

    /**
     * Resolve a {@code #include "name"} directive.
     * Looks in the current file's directory first; if not found there, falls
     * back to the configured {@link #myIncludePaths}.
     */
    private void processInclude(@Nullable String includeName) {
        if (includeName == null) {
            return;
        }

        VirtualFile currentVFile = myPreprocessorFile.getVirtualFile();
        if (currentVFile == null) {
            // LightVirtualFile (test / in-memory context) — cannot resolve paths
            return;
        }

        VirtualFile parent = currentVFile.getParent();
        if (parent != null) {
            // Source 1: file-relative directory
            processIncludeFromDir(includeName, parent);
        }
        else {
            // No parent directory — fall straight through to include search paths
            processIndependInclude(includeName);
        }
    }

    /**
     * Resolve a {@code #include <name>} directive (or a {@code #include "name"}
     * that was not found in the file-relative directory).
     * Searches only the configured {@link #myIncludePaths}.
     */
    private void processIndependInclude(@Nullable String includeName) {
        if (includeName == null) {
            return;
        }

        // Source 2: configured include search paths
        for (VirtualFile searchDir : myIncludePaths) {
            VirtualFile found = searchDir.findFileByRelativePath(includeName);
            if (found != null) {
                processResolvedFile(found);
                return;
            }
        }
    }

    /**
     * Resolve {@code includeName} relative to {@code fromDir}.
     * If the file is not found there, falls back to
     * {@link #processIndependInclude} (the configured include paths).
     * <p>
     * Used both for the top-level {@code #include "..."} in the current file
     * and for transitive includes encountered while walking stub trees.
     *
     * @param includeName relative path as written between the quotes / angles
     * @param fromDir     directory from which the path should be resolved first
     */
    private void processIncludeFromDir(@Nullable String includeName, @NotNull VirtualFile fromDir) {
        if (includeName == null) {
            return;
        }

        // Source 1: directory-relative lookup
        VirtualFile found = fromDir.findFileByRelativePath(includeName);
        if (found != null) {
            processResolvedFile(found);
            return;
        }

        // Source 2: fall back to configured include search paths
        processIndependInclude(includeName);
    }

    /**
     * Given a resolved {@link VirtualFile}, perform cycle detection and then
     * read its macro definitions — preferring the stub index to avoid full
     * PSI loading, with a PSI fallback when stubs are not yet available.
     *
     * @param includedVFile the file whose macros should be merged into
     *                      {@link #myDefines}
     */
    private void processResolvedFile(@NotNull VirtualFile includedVFile) {
        // Cycle detection: skip files we are already expanding
        if (!myProcessedFiles.add(includedVFile)) {
            return;
        }

        // Prefer stubs — safe during indexing, no cascading PSI loads
        Project project = myPreprocessorFile.getProject();
        ObjectStubTree<?> stubTree = StubTreeLoader.getInstance().readOrBuild(project, includedVFile, null);
        if (stubTree != null) {
            Stub root = stubTree.getRoot();
            if (root instanceof CPreprocessorFileStub) {
                processDefinesFromFileStub((CPreprocessorFileStub) root, includedVFile);
                return;
            }
        }

        // Fall back to full PSI loading (e.g. during editing before stubs are built)
        PsiFile includedPsiFile = myPreprocessorFile.getManager().findFile(includedVFile);
        if (includedPsiFile == null) {
            return;
        }

        // Only C/C++ files have a CPreprocessorLanguage root — skip everything else
        PsiFile preprocessorRoot = includedPsiFile.getViewProvider().getPsi(CPreprocessorLanguage.INSTANCE);
        if (preprocessorRoot == null) {
            return;
        }

        // Recursively expand the included file, sharing the processed-files set and include paths
        PreprocessorExpander childExpander =
                new PreprocessorExpander(preprocessorRoot, myParserDefinition, myProcessedFiles, myIncludePaths, definesView());
        // The child started from our defines, so its final state is the state after the include: names it
        // undefined are gone, the rest are added or overridden.
        myDefines.keySet().retainAll(childExpander.myDefines.keySet());
        myDefines.putAll(childExpander.myDefines);
    }

    /**
     * Walk the direct stub children of a preprocessor file stub and collect
     * macro definitions into {@link #myDefines}, recursively following any
     * {@code #include} stubs for transitive header chains.
     *
     * @param fileStub      the root stub of the included file
     * @param includedVFile the virtual file that owns {@code fileStub} (used to
     *                      resolve relative paths in transitive {@code #include}s)
     */
    private void processDefinesFromFileStub(@NotNull CPreprocessorFileStub fileStub,
                                            @NotNull VirtualFile includedVFile) {
        processStubChildren(fileStub, includedVFile.getParent());
    }

    /**
     * Walks a stub subtree applying its directives in order. Conditionals are evaluated against the defines
     * gathered so far and only the active branch is descended into, so a header contributes the defines of the
     * branch that is really compiled rather than of every branch it contains.
     */
    private void processStubChildren(@NotNull StubElement<?> parent, @Nullable VirtualFile dir) {
        for (StubElement<?> child : parent.getChildrenStubs()) {
            if (child instanceof CPreprocessorDefineStub) {
                CPreprocessorDefineStub defineStub = (CPreprocessorDefineStub) child;
                String name = defineStub.getName();
                String valueText = defineStub.getValueText();
                if (name != null) {
                    myDefines.put(name, new ExpandedMacro(myParserDefinition, null, valueText != null ? valueText : ""));
                }
            }
            else if (child instanceof CPreprocessorUndefStub) {
                String name = ((CPreprocessorUndefStub) child).getName();
                if (name != null) {
                    myDefines.remove(name);
                }
            }
            else if (child instanceof CPreprocessorIncludeStub && dir != null) {
                processIncludeFromDir(((CPreprocessorIncludeStub) child).getIncludePath(), dir);
            }
            else if (child instanceof CPreprocessorIfBlockStub) {
                CPreprocessorIfBlockStub block = (CPreprocessorIfBlockStub) child;
                String name = block.getVariableName();
                boolean defined = name != null && myDefines.containsKey(name);
                boolean thenActive = defined != block.isReverted();

                for (StubElement<?> body : block.getChildrenStubs()) {
                    if (body instanceof CPreprocessorIfBodyStub
                        && ((CPreprocessorIfBodyStub) body).isElseBody() != thenActive) {
                        processStubChildren(body, dir);
                    }
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    private Map<String, String> definesView() {
        Map<String, String> view = new LinkedHashMap<>();
        for (Map.Entry<String, ExpandedMacro> entry : myDefines.entrySet()) {
            view.put(entry.getKey(), entry.getValue().getText().toString());
        }
        return view;
    }

    /**
     * Picks the branch of a conditional that is really compiled. The block is a flat run of keyword, condition
     * and body, so the branches are scanned in source order and the first one whose condition holds wins -
     * which is what makes {@code #elif} chains behave, with {@code #else} as the branch that always holds.
     */
    private CPsiSharpIfBody selectActiveBody(CPreprocessorIfBlock element) {
        Map<String, String> defines = definesView();
        BranchKind kind = null;
        StringBuilder condition = new StringBuilder();

        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            IElementType type = child.getNode().getElementType();

            if (type == CPreprocessorTokenTypes.S_IF_KEYWORD) {
                kind = BranchKind.IF;
                condition.setLength(0);
            }
            else if (type == CPreprocessorTokenTypes.S_IFDEF_KEYWORD) {
                kind = BranchKind.IFDEF;
                condition.setLength(0);
            }
            else if (type == CPreprocessorTokenTypes.S_IFNDEF_KEYWORD) {
                kind = BranchKind.IFNDEF;
                condition.setLength(0);
            }
            else if (type == CPreprocessorTokenTypes.S_ELIF_KEYWORD) {
                kind = BranchKind.ELIF;
                condition.setLength(0);
            }
            else if (type == CPreprocessorTokenTypes.S_ELSE_KEYWORD) {
                kind = BranchKind.ELSE;
                condition.setLength(0);
            }
            else if (child instanceof CPsiSharpIfBody) {
                if (kind != null && isBranchActive(kind, condition.toString(), defines)) {
                    return (CPsiSharpIfBody) child;
                }
                kind = null;
            }
            else if (kind != null) {
                condition.append(child.getText());
            }
        }
        return null;
    }

    private static boolean isBranchActive(BranchKind kind, String condition, Map<String, String> defines) {
        return switch (kind) {
            case IFDEF -> defines.containsKey(condition.trim());
            case IFNDEF -> !defines.containsKey(condition.trim());
            case ELSE -> true;
            case IF, ELIF -> CPreprocessorConditionEvaluator.evaluate(condition, defines);
        };
    }

    private enum BranchKind {
        IF,
        IFDEF,
        IFNDEF,
        ELIF,
        ELSE
    }

    private void addOuterRange(int startOffset, int endOffset) {
        if (endOffset > startOffset) {
            myModifications.addOuterRange(new TextRange(startOffset, endOffset));
        }
    }

    /**
     * Ranges of the conditional branches which this expansion excluded, for painting them as inactive code.
     */
    public List<TextRange> getInactiveRanges() {
        return myInactiveRanges;
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
    public ExpandedMacro tryToExpand(String name) {
        ExpandedMacro macro = myDefines.get(name);
        if (macro == null) {
            return null;
        }

        macro.expand();
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

            for (PreprocessorSymbolDoneInfo doneInfo : value.getSymbolDoneInfos()) {
                Pair<IElementType, String> symbol = entry.getValue().getSymbols().get(doneInfo.getTokenIndex());

                int tokenOffset = doneInfo.getTokenOffset();

                TextRange textRange = new TextRange(tokenOffset, tokenOffset + key.length());

                TreeElement node = (TreeElement) findNode(textRange, parsed, true);

                Couple<TreeElement> forInsert = selectNodeForInsert(node, node, doneInfo.getDoneElementType());

                TreeElement parentIn = forInsert.getFirst();

                TreeElement childIn = forInsert.getSecond();

                if (childIn.getPsi() instanceof CPreprocessorMacroReference) {
                    childIn.rawInsertAfterMe(new CPreprocessorForeignLeafPsiElement(symbol.getFirst(), symbol.second));
                }
                else {
                    childIn.rawInsertBeforeMe(new CPreprocessorForeignLeafPsiElement(symbol.getFirst(), symbol.second));
                }
            }
        }
    }

    private static Couple<TreeElement> selectNodeForInsert(TreeElement element, TreeElement childElement, AstElementTypeId astElementTypeId) {
        IElementType elementType = element.getElementType();

        if (elementType == astElementTypeId.getElementType() && element.getStartOffset() == astElementTypeId.getStartOffset()) {
            return Couple.of(element, childElement);
        }

        CompositeElement treeParent = element.getTreeParent();
        if (treeParent == null) {
            return null;
        }
        return selectNodeForInsert(treeParent, element, astElementTypeId);
    }

    private static ASTNode findNode(TextRange textRange, ASTNode parsed, boolean strict) {
        ASTNode node = parsed;

        while (node != null) {
            if (node instanceof LeafElement) {
                if (strict) {
                    if (textRange.equals(node.getTextRange())) {
                        return node;
                    }
                }
                else {
                    if (textRange.contains(node.getStartOffset())) {
                        return node;
                    }
                }
            }
            else {
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
