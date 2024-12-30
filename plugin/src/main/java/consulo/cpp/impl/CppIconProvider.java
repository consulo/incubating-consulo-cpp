package consulo.cpp.impl;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.language.icon.IconDescriptor;
import consulo.language.icon.IconDescriptorUpdater;
import consulo.language.psi.PsiElement;
import org.napile.cpp4idea.lang.psi.CPsiClass;
import org.napile.cpp4idea.lang.psi.CPsiDeclarationMethod;

import jakarta.annotation.Nonnull;

@ExtensionImpl
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
