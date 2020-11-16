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

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.ide.projectView.nodes.CClassTreeNode;
import org.napile.cpp4idea.ide.projectView.nodes.CMethodTreeNode;
import org.napile.cpp4idea.lang.psi.CPsiClass;
import org.napile.cpp4idea.lang.psi.CPsiDeclaration;
import org.napile.cpp4idea.lang.psi.CPsiDeclarationMethod;
import org.napile.cpp4idea.lang.psi.CPsiFile;

import java.util.List;

/**
 * @author VISTALL
 * @date 17:23/07.01.13
 */
public class CProjectViewUtil {
	@Nullable
	public static CPsiClass findSingleClass(@NotNull CPsiFile f) {
		CPsiDeclaration[] declarations = f.getDeclarations();
		return declarations.length == 1 && declarations[0] instanceof CPsiClass ? (CPsiClass) declarations[0] : null;
	}

	public static void addChildren(Project p, ViewSettings viewSettings, List<AbstractTreeNode> list, CPsiDeclaration[] declarations) {
		for (CPsiDeclaration declaration : declarations) {
			if (declaration instanceof CPsiClass) {
				list.add(new CClassTreeNode(p, (CPsiClass) declaration, viewSettings));
			} else if (declaration instanceof CPsiDeclarationMethod) {
				list.add(new CMethodTreeNode(p, (CPsiDeclarationMethod) declaration, viewSettings));
			}
		}
	}
}
