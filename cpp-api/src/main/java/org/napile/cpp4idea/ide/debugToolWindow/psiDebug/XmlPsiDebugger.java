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

package org.napile.cpp4idea.ide.debugToolWindow.psiDebug;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @date 15:29/29.12.12
 */
public class XmlPsiDebugger {
	public XmlPsiDebugger() {
	}

	public String toText(@NotNull PsiElement element) {
		StringBuilder builder = new StringBuilder();
		append(builder, element, 0);
		return builder.toString();
	}

	private void append(StringBuilder builder, PsiElement psiElement, int indent) {
		StringUtil.repeatSymbol(builder, '\t', indent);

		String tagName = psiElement.getClass().getSimpleName();
		if (tagName.endsWith("Impl")) {
			tagName = tagName.substring(0, tagName.length() - 4);
		}

		builder.append("<").append(tagName).append(" text=\"").append(psiElement.getText().replace("\n", "\\n")).append("\"");
		PsiElement child = psiElement.getFirstChild();
		if (child != null) {
			builder.append(">\n");
			while (child != null) {
				append(builder, child, indent + 1);

				child = child.getNextSibling();
			}

			StringUtil.repeatSymbol(builder, '\t', indent);
			builder.append("</").append(tagName).append(">\n");
		}
		else {
			builder.append(" />\n");
		}
	}
}
