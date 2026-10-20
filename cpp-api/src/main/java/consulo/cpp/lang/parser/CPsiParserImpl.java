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

import consulo.cpp.lang.version.CLanguageVersion;
import consulo.cpp.lang.version.CLanguageVersions;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiParser;
import consulo.language.version.LanguageVersion;

/**
 * @author VISTALL
 * @date 14:28/18.12.2011
 */
public class CPsiParserImpl implements PsiParser {
    @Override
    public ASTNode parse(IElementType root, PsiBuilder builder, LanguageVersion languageVersion) {
        builder.setDebugMode(true);

        CLanguageVersion cLanguageVersion = languageVersion instanceof CLanguageVersion version ? version : CLanguageVersions.getDefaultCpp();

        PsiBuilder.Marker rootMarker = builder.mark();
        createContext(builder, cLanguageVersion).getDeclarationParsing().parseFile();
        rootMarker.done(root);

        return builder.getTreeBuilt();
    }

    /**
     * A dialect returns a context of its own to replace parts of the grammar.
     */
    protected CParsingContext createContext(PsiBuilder builder, CLanguageVersion languageVersion) {
        return new CParsingContext(builder, languageVersion);
    }
}
