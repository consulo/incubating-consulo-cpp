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

package org.napile.cpp4idea.lang.parser.parsingMain;

import consulo.cpp.localize.CLocalize;
import consulo.language.parser.PsiBuilder;
import org.napile.cpp4idea.lang.psi.CElementTypes;
import org.napile.cpp4idea.lang.psi.CPsiTokens;

/**
 * @author VISTALL
 * @date 14:38/11.12.2011
 */
public class ParameterListParsing extends MainParsing {
    public static void parseParameterList(PsiBuilder builder) {
        PsiBuilder.Marker marker;

        if (builder.getTokenType() != LPARENTH) {
            builder.error(CLocalize.lparenthExpected().get());
            marker = builder.mark();
            builder.advanceLexer();
            marker.done(CElementTypes.PARAMETER_LIST);
            return;
        }
        else {
            marker = builder.mark();
            advanceLexerAndSkipLines(builder);
        }

        if (builder.getTokenType() != RPARENTH) {
            while (true) {
                if (builder.getTokenType() == ELLIPSIS) {
                    doneOneToken(builder, CElementTypes.PARAMETER);
                }
                else {
                    VariableParsing.parseVariable(CElementTypes.PARAMETER, builder);
                }

                if (!consumeIf(builder, COMMA)) {
                    break;
                }
            }

            expect(builder, RPARENTH, CLocalize.rparenthExpected());
        }
        else {
            advanceLexerAndSkipLines(builder);
        }

        marker.done(CElementTypes.PARAMETER_LIST);
    }

    private static void parserParameter(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        // goto parameter name
        parseTypeRef(builder);

        if (builder.getTokenType() == IDENTIFIER) {
            advanceLexerAndSkipLines(builder);
        }

        if (builder.getTokenType() == CPsiTokens.LBRACKET) {
            advanceLexerAndSkipLines(builder);
            expect(builder, CPsiTokens.RBRACKET, CLocalize.rbracketExpected());
        }

        marker.done(CElementTypes.PARAMETER);

        while (builder.getTokenType() == COMMA) {
            advanceLexerAndSkipLines(builder); // skip COMMA

            parserParameter(builder);
        }
    }
}
