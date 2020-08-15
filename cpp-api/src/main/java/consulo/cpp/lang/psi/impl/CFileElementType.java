package consulo.cpp.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.psi.*;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.SharedImplUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.CharTable;
import com.intellij.util.LocalTimeCounter;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.expand.CPreprocessorDirectiveCollector;
import consulo.cpp.preprocessor.expand.PreprocessorExpander;
import consulo.cpp.preprocessor.fileProvider.CFileViewProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.CLanguage;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 15:16/2020-07-31
 *
 * @see com.intellij.psi.templateLanguages.TemplateDataElementType
 */
public class CFileElementType extends IStubFileElementType<PsiFileStub<PsiFile>> {
	public static final CFileElementType INSTANCE = new CFileElementType();

	public CFileElementType() {
		super(CLanguage.INSTANCE);
	}

	@Override
	protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
		final CharTable charTable = SharedImplUtil.findCharTableByTree(chameleon);

		CFileViewProvider viewProvider = (CFileViewProvider) ((PsiFile) psi).getViewProvider();

		PsiFile preprocessorFile = viewProvider.getPsi(CPreprocessorLanguage.INSTANCE);

		ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.findSingle(CLanguage.INSTANCE);

		PreprocessorExpander expander = new PreprocessorExpander(preprocessorFile, parserDefinition);

		CharSequence sourceCode = chameleon.getChars();

		CharSequence onlyCSourceCode = expander.buildText(sourceCode);

		CPreprocessorDirectiveCollector collector = expander.getRangeCollector();

		final PsiFile templatePsiFile = createPsiFileFromSource(CLanguage.INSTANCE, onlyCSourceCode, psi.getManager(), expander);
		final FileElement templateFileElement = ((PsiFileImpl) templatePsiFile).calcTreeElement();

		return DebugUtil.performPsiModification("template language parsing", () -> {
			expander.insertDummyNodes(templateFileElement);

			collector.insertOuterElementsAndRemoveRanges(templateFileElement, sourceCode, charTable, templateFileElement.getPsi().getLanguage());

			TreeElement childNode = templateFileElement.getFirstChildNode();

			DebugUtil.checkTreeStructure(templateFileElement);
			DebugUtil.checkTreeStructure(chameleon);

			return childNode;
		});
	}

	protected PsiFile createPsiFileFromSource(final Language language, CharSequence sourceCode, PsiManager manager, PreprocessorExpander expander) {
		LightVirtualFile virtualFile = new LightVirtualFile("foo", createTemplateFakeFileType(language), sourceCode, LocalTimeCounter.currentTime());

		FileViewProvider viewProvider = new SingleRootFileViewProvider(manager, virtualFile, false) {
			@Override
			@NotNull
			public Language getBaseLanguage() {
				return language;
			}

			@Override
			protected @Nullable PsiFile createFile(@NotNull Language lang) {
				PsiFile file = super.createFile(lang);
				if (file == null) {
					return null;
				}

				PsiFileImpl fileImpl = (PsiFileImpl) file;
				fileImpl.setContentElementType(CFileInnerElementType.INSTANCE);
				return fileImpl;
			}
		};

		viewProvider.putUserData(PreprocessorExpander.EXPANDER_KEY, expander);

		// Since we're already inside a template language PSI that was built regardless of the file size (for whatever reason),
		// there should also be no file size checks for template data files.
		SingleRootFileViewProvider.doNotCheckFileSizeLimit(virtualFile);

		return viewProvider.getPsi(language);
	}

	protected LanguageFileType createTemplateFakeFileType(final Language language) {
		return new TemplateFileType(language);
	}

	protected static class TemplateFileType extends LanguageFileType {
		private final Language myLanguage;

		protected TemplateFileType(final Language language) {
			super(language);
			myLanguage = language;
		}

		@Override
		@NotNull
		public String getDefaultExtension() {
			return "";
		}

		@Override
		@NotNull
		@NonNls
		public String getDescription() {
			return "fake for language" + myLanguage.getID();
		}

		@Override
		@Nullable
		public Icon getIcon() {
			return null;
		}

		@Override
		@NotNull
		@NonNls
		public String getName() {
			return myLanguage.getID();
		}
	}
}
