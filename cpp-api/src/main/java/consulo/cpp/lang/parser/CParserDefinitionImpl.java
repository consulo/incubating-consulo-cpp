/*
 * Copyright 2011 napile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.cpp.lang.parser;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.psi.impl.CFileElementType;
import consulo.cpp.lang.psi.impl.CFileImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementTypeAsPsiFactory;
import consulo.language.ast.IFileElementType;
import consulo.language.file.FileViewProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersionableParserDefinition;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import consulo.cpp.lang.CLanguage;

/**
 * @author VISTALL
 * @date 14:26/18.12.2011
 */
@ExtensionImpl
public class CParserDefinitionImpl extends LanguageVersionableParserDefinition {
    @Nonnull
    @Override
    public Language getLanguage() {
        return CLanguage.INSTANCE;
    }

    @Override
    public IFileElementType getFileNodeType() {
        return CFileElementType.INSTANCE;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        // element types build their own PSI, which is what lets a dialect plugin add nodes of its own
        if (node.getElementType() instanceof IElementTypeAsPsiFactory factory) {
            return factory.createElement(node);
        }

        throw new IllegalArgumentException("Illegal argument : " + node.getElementType());
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new CFileImpl(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
