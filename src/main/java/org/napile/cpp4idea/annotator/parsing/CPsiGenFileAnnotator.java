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

package org.napile.cpp4idea.annotator.parsing;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.parsing.second.lexer.CFlexLexer;
import org.napile.cpp4idea.lang.parsing.second.parser.CParserDefinitionImpl;
import org.napile.cpp4idea.lang.psi.CPsiRawFile;
import org.napile.cpp4idea.lang.psi.impl.wrapper.PsiElementWrapper;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.impl.PsiBuilderFactoryImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @date 16:27/15.12.2011
 * TODO [VISTALL] link Set<String> to CPsiGenFile - if new var list is not equals - remake all gen files
 */
public class CPsiGenFileAnnotator implements Annotator
{
	private static final PsiBuilderFactory FACTORY = new PsiBuilderFactoryImpl();

	@Override
	public void annotate(@NotNull PsiElement element, final @NotNull AnnotationHolder holder)
	{
		if(!(element instanceof CPsiRawFile))
			return;

		CPsiRawFile file = (CPsiRawFile)element;

		if(file.getGenFile() == null)
			file.buildGen(new HashSet<String>(), new HashSet<PsiFile>());

		StringBuilder builder = new StringBuilder();
		for(PsiElement e : file.getGenFile().getChildren())
		{
			if(e instanceof PsiElementWrapper && ((PsiElementWrapper) e).isDefined())
				builder.append(e.getText());
		}

		com.intellij.openapi.project.Project p = element.getProject();

		FACTORY.createBuilder(new CParserDefinitionImpl(), new CFlexLexer(), builder.toString()).getTreeBuilt();
	}
}
