package consulo.cpp.impl;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import consulo.annotation.access.RequiredReadAction;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.cpp4idea.lang.psi.CPsiClass;
import org.napile.cpp4idea.lang.psi.CPsiDeclarationMethod;

import javax.annotation.Nonnull;
import javax.swing.*;

public class CppIconProvider implements IconDescriptorUpdater
{
	@RequiredReadAction
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int flags)
	{
		if(element instanceof CPsiClass)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Class);
		}
		if(element instanceof CPsiDeclarationMethod)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Method);
		}
	}
}
