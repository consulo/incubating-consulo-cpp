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

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.napile.cpp4idea.lang.psi.CPsiClass;
import org.napile.cpp4idea.lang.psi.CPsiDeclarationMethod;

/**
 * @author VISTALL
 * @date 17:34/07.01.13
 */
public class CIconProvider implements IconDescriptorUpdater {
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int i) {
		if (element instanceof CPsiClass) {
			iconDescriptor.setMainIcon(AllIcons.Nodes.Class);
		}
		if (element instanceof CPsiDeclarationMethod) {
			iconDescriptor.setMainIcon(AllIcons.Nodes.Method);
		}
	}
}
