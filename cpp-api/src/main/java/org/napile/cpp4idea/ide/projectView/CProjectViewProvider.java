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

package org.napile.cpp4idea.ide.projectView;

import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.ide.projectView.nodes.CClassTreeNode;
import org.napile.cpp4idea.ide.projectView.nodes.CFileTreeNode;
import org.napile.cpp4idea.lang.psi.CPsiClass;
import org.napile.cpp4idea.lang.psi.CPsiFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @date 17:19/07.01.13
 */
public class CProjectViewProvider implements SelectableTreeStructureProvider, DumbAware {
	@Nullable
	@Override
	public PsiElement getTopLevelElement(PsiElement element) {
		if (element instanceof CPsiFile) {
			CPsiClass clazz = CProjectViewUtil.findSingleClass(((CPsiFile) element));
			if (clazz != null) {
				return clazz;
			}
			return element;
		}
		return null;
	}

	@Override
	public @NotNull Collection<AbstractTreeNode<?>> modify(AbstractTreeNode<?> parent, Collection<AbstractTreeNode<?>> children, ViewSettings settings) {
		List<AbstractTreeNode<?>> result = new ArrayList<>(children.size());

		for (AbstractTreeNode<?> child : children) {
			Object childValue = child.getValue();

			if (childValue instanceof CPsiFile) {
				CPsiFile file = (CPsiFile) childValue;
				CPsiClass clazz = CProjectViewUtil.findSingleClass(((CPsiFile) childValue));
				if (clazz != null) {
					result.add(new CClassTreeNode(file.getProject(), clazz, settings));
				} else {
					result.add(new CFileTreeNode(file.getProject(), file, settings));
				}
			} else {
				result.add(child);
			}
		}

		return result;
	}
}
