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

package org.napile.cpp4idea.lang.psiInitial;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.preprocessor.CPreprocessor;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import com.intellij.openapi.util.NotNullLazyKey;
import com.intellij.psi.PsiFile;
import com.intellij.util.NotNullFunction;

/**
 * @author VISTALL
 * @date 1:07/10.12.2011
 */
public interface CPsiSharpFile extends PsiFile {
	NotNullLazyKey<CPsiFile, CPsiSharpFile> AFTER_PROCESSED_FILE = NotNullLazyKey.create("after-processed-file", new NotNullFunction<CPsiSharpFile, CPsiFile>() {
		@NotNull
		@Override
		public CPsiFile fun(CPsiSharpFile dom) {
			return CPreprocessor.preProcess(dom);
		}
	});


	boolean isSourceFile();
}
