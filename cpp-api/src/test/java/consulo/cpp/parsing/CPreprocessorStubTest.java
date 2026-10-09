/*
 * Copyright 2013-2026 consulo.io
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
package consulo.cpp.parsing;

import consulo.cpp.CParsingBaseTest;
import consulo.cpp.lang.CSourceFileType;
import consulo.cpp.preprocessor.CPreprocessorLanguage;
import consulo.cpp.preprocessor.psi.CPreprocessorFileElementType;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorDefineStub;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBlockStub;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIfBodyStub;
import consulo.cpp.preprocessor.psi.stub.CPreprocessorIncludeStub;
import consulo.language.file.LanguageFileType;
import consulo.language.psi.PsiFile;
import consulo.language.psi.stub.StubElement;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The stub tree is what the index stores for a preprocessor file, so these assertions cover the data every
 * index lookup of a define or an include ultimately reads.
 */
public class CPreprocessorStubTest extends CParsingBaseTest {
    public CPreprocessorStubTest() {
        super("parsing/preprocessor", "c");
    }

    @Nonnull
    @Override
    protected LanguageFileType getFileType(@Nonnull Context context, @Nullable Object testContext) {
        return CSourceFileType.INSTANCE;
    }

    @Test
    public void definesAndIncludesReachTheStub(Context context) {
        StubElement<?> root = buildStubTree(context, "#define A 1\n#define B\n#include \"x.h\"\n");

        List<String> defines = new ArrayList<>();
        List<String> includes = new ArrayList<>();
        collect(root, defines, includes);

        assertEquals(List.of("A", "B"), defines);
        assertEquals(List.of("x.h"), includes);
    }

    /**
     * An object-like macro with no replacement text still has to be stubbed, otherwise an include guard or a
     * feature flag would be invisible to {@code #ifdef}.
     */
    @Test
    public void valuelessDefineIsStubbed(Context context) {
        StubElement<?> root = buildStubTree(context, "#define GUARD\n");

        List<String> defines = new ArrayList<>();
        collect(root, defines, new ArrayList<>());

        assertEquals(List.of("GUARD"), defines);
    }

    /**
     * Defines guarded by a conditional must sit under their branch rather than flat under the file, otherwise a
     * file including this one would pick up the defines of every branch instead of the active one.
     */
    @Test
    public void conditionalDefinesAreNestedUnderTheirBranch(Context context) {
        StubElement<?> root = buildStubTree(context, "#ifdef X\n#define A 1\n#else\n#define B 2\n#endif\n");

        List<String> flatDefines = new ArrayList<>();
        for (StubElement<?> child : root.getChildrenStubs()) {
            if (child instanceof CPreprocessorDefineStub define) {
                flatDefines.add(define.getName());
            }
        }
        assertEquals(List.of(), flatDefines);

        CPreprocessorIfBlockStub block = null;
        for (StubElement<?> child : root.getChildrenStubs()) {
            if (child instanceof CPreprocessorIfBlockStub candidate) {
                block = candidate;
            }
        }
        assertNotNull(block);
        assertEquals("X", block.getVariableName());
        assertFalse(block.isReverted());

        List<String> thenDefines = new ArrayList<>();
        List<String> elseDefines = new ArrayList<>();
        for (StubElement<?> body : block.getChildrenStubs()) {
            if (body instanceof CPreprocessorIfBodyStub ifBody) {
                collect(body, ifBody.isElseBody() ? elseDefines : thenDefines, new ArrayList<>());
            }
        }
        assertEquals(List.of("A"), thenDefines);
        assertEquals(List.of("B"), elseDefines);
    }

    private StubElement<?> buildStubTree(Context context, String text) {
        PsiFile file = createFile(context, null, "Stubbed.c", CSourceFileType.INSTANCE, text);
        PsiFile preprocessorFile = (PsiFile)file.getViewProvider().getPsi(CPreprocessorLanguage.INSTANCE);
        return CPreprocessorFileElementType.INSTANCE.getBuilder().buildStubTree(preprocessorFile);
    }

    private static void collect(StubElement<?> stub, List<String> defines, List<String> includes) {
        if (stub instanceof CPreprocessorDefineStub define) {
            defines.add(define.getName());
        }
        if (stub instanceof CPreprocessorIncludeStub include) {
            includes.add(include.getIncludePath());
        }
        for (StubElement<?> child : stub.getChildrenStubs()) {
            collect(child, defines, includes);
        }
    }
}
