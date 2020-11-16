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

import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import consulo.ide.IconDescriptorUpdaters;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiDeclaration;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @date 19:06/07.01.13
 */
public abstract class CBaseTreeNode<D extends CPsiDeclaration> extends ProjectViewNode<D> {
	public CBaseTreeNode(Project project, D d, ViewSettings viewSettings) {
		super(project, d, viewSettings);
	}

	@NotNull
	@Override
	public Collection<? extends AbstractTreeNode<?>> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public boolean canNavigate() {
		return true;
	}

	@Override
	public boolean contains(@NotNull VirtualFile file) {
		return false;
	}

	@Override
	public void navigate(boolean requestFocus) {
		CPsiDeclaration declaration = getValue();

		PsiElement nameIdentifier = declaration.getNameIdentifier();
		if (nameIdentifier != null) {
			NavigationUtil.activateFileWithPsiElement(nameIdentifier, true);
		}
	}

	@Override
	protected void update(PresentationData presentation) {
		presentation.setPresentableText(getValue().getName());

		presentation.setIcon(IconDescriptorUpdaters.getIcon(getValue(), 0));
	}
}
