package consulo.cpp.impl;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiClass;
import org.napile.cpp4idea.lang.psi.CPsiDeclarationMethod;

import javax.swing.*;

public class CppIconProvider extends IconProvider {
	@Override
	public @Nullable Icon getIcon(@NotNull PsiElement element, int flags) {
		if (element instanceof CPsiClass) {
			return AllIcons.Nodes.Class;
		}
		if (element instanceof CPsiDeclarationMethod) {
			return AllIcons.Nodes.Method;
		}
		return null;
	}
}
