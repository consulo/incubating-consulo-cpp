/*
 * Copyright 2010-2012 napile.org
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

package consulo.cpp.preprocessor.psi.impl.visitor;

import consulo.cpp.preprocessor.psi.CPsiSharpDefineValue;
import consulo.cpp.preprocessor.psi.CPsiSharpElement;
import consulo.cpp.preprocessor.psi.CPsiSharpFile;
import consulo.cpp.preprocessor.psi.CPsiCompilerVariable;
import consulo.cpp.preprocessor.psi.CPsiSharpDefine;
import consulo.cpp.preprocessor.psi.CPsiSharpIfBody;
import consulo.cpp.preprocessor.psi.CPsiSharpIfDef;
import consulo.cpp.preprocessor.psi.CPsiSharpInclude;
import consulo.cpp.preprocessor.psi.CPsiSharpIndepInclude;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author VISTALL
 * @since 13:07/16.12.2011
 */
public class CPreprocessorElementVisitor extends PsiElementVisitor {
	public void visitSFile(CPsiSharpFile file) {
		visitFile(file);
	}

	public void visitSElement(CPsiSharpElement element) {
		visitElement(element);
	}

	public void visitCompilerVariable(CPsiCompilerVariable element) {
		visitSElement(element);
	}

	public void visitSIfBody(CPsiSharpIfBody element) {
		visitSElement(element);
	}

	public void visitSDefine(CPsiSharpDefine element) {
		visitSElement(element);
	}

	public void visitSDefineValue(CPsiSharpDefineValue element) {
		visitSElement(element);
	}

	public void visitSIfDef(CPsiSharpIfDef element) {
		visitSElement(element);
	}

	public void visitSInclude(CPsiSharpInclude element) {
		visitSElement(element);
	}

	public void visitSIndependInclude(CPsiSharpIndepInclude element) {
		visitSElement(element);
	}
}
