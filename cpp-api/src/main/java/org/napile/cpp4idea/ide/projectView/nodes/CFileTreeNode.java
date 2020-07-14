/*
 * Copyright 2010-2013 napile.org
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

package org.napile.cpp4idea.ide.projectView.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.napile.cpp4idea.ide.projectView.CProjectViewUtil;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @date 17:27/07.01.13
 */
public class CFileTreeNode extends PsiFileNode {
	public CFileTreeNode(Project project, PsiFile value, ViewSettings viewSettings) {
		super(project, value, viewSettings);
	}

	@Override
	public Collection<AbstractTreeNode<?>> getChildrenImpl() {
		CPsiSharpFile file = (CPsiSharpFile) getValue();

		if (file == null || !getSettings().isShowMembers())
			return Collections.emptyList();

		CPsiFile pFile = CPsiSharpFile.AFTER_PROCESSED_FILE.getValue(file);

		List<AbstractTreeNode<?>> result = new ArrayList<AbstractTreeNode<?>>();

		CProjectViewUtil.addChildren(getProject(), getSettings(), result, pFile.getDeclarations());
		return result;
	}
}
