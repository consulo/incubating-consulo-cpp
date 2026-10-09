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
package consulo.cpp.preprocessor.psi;

/**
 * Every stub element type of the preprocessor language, and nothing else.
 * <p>
 * The platform requires these to exist before index initialisation finishes, and it discovers them through
 * {@link CPreprocessorStubElementTypeHolder}. A type missing here is only noticed when the language is indexed
 * for the first time, as a failure to find an id for its serializer.
 */
public interface CPreprocessorStubElementTypes {
    CPreprocessorFileElementType FILE = CPreprocessorFileElementType.INSTANCE;

    CPreprocessorDefineStubElementType DEFINE = CPreprocessorDefineStubElementType.INSTANCE;

    CPreprocessorUndefStubElementType UNDEF = CPreprocessorUndefStubElementType.INSTANCE;

    CPreprocessorIncludeStubElementType INCLUDE = CPreprocessorIncludeStubElementType.INSTANCE;

    CPreprocessorIfBlockStubElementType IF_BLOCK = CPreprocessorIfBlockStubElementType.INSTANCE;

    CPreprocessorIfBodyStubElementType IF_BODY = CPreprocessorIfBodyStubElementType.INSTANCE;
}
