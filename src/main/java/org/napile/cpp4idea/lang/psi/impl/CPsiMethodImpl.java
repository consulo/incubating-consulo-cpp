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
 *    limitations under the License.
 */

package org.napile.cpp4idea.lang.psi.impl;

import java.util.List;

import javax.swing.Icon;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.util.IncorrectOperationException;

/**
 * @author VISTALL
 * @date 5:44/10.12.2011
 */
public class CPsiMethodImpl implements PsiMethod
{
	public CPsiMethodImpl(ASTNode node)
	{
		//super(node);
	}

	@Override
	public PsiType getReturnType()
	{
		return null;
	}

	@Override
	public PsiTypeElement getReturnTypeElement()
	{
		return null;
	}

	@NotNull
	@Override
	public PsiParameterList getParameterList()
	{
		return null;
	}

	@NotNull
	@Override
	public PsiReferenceList getThrowsList()
	{
		return null;
	}

	@Override
	public PsiCodeBlock getBody()
	{
		return null;
	}

	@Override
	public boolean isConstructor()
	{
		return false;
	}

	@Override
	public boolean isVarArgs()
	{
		return false;
	}

	@NotNull
	@Override
	public MethodSignature getSignature(@NotNull PsiSubstitutor substitutor)
	{
		return null;
	}

	@Override
	public PsiIdentifier getNameIdentifier()
	{
		return null;
	}

	@NotNull
	@Override
	public PsiMethod[] findSuperMethods()
	{
		return new PsiMethod[0];
	}

	@NotNull
	@Override
	public PsiMethod[] findSuperMethods(boolean checkAccess)
	{
		return new PsiMethod[0];
	}

	@NotNull
	@Override
	public PsiMethod[] findSuperMethods(PsiClass parentClass)
	{
		return new PsiMethod[0];
	}

	@NotNull
	@Override
	public List<MethodSignatureBackedByPsiMethod> findSuperMethodSignaturesIncludingStatic(boolean checkAccess)
	{
		return null;
	}

	@Override
	public PsiMethod findDeepestSuperMethod()
	{
		return null;
	}

	@NotNull
	@Override
	public PsiMethod[] findDeepestSuperMethods()
	{
		return new PsiMethod[0];
	}

	@NotNull
	@Override
	public PsiModifierList getModifierList()
	{
		return null;
	}

	@Override
	public boolean hasModifierProperty(@Modifier @NonNls @NotNull String name)
	{
		return false;
	}

	@NotNull
	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return null;
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		return null;
	}

	@NotNull
	@Override
	public HierarchicalMethodSignature getHierarchicalMethodSignature()
	{
		return null;
	}

	@Override
	public PsiMethodReceiver getMethodReceiver()
	{
		return null;
	}

	@Override
	public PsiType getReturnTypeNoResolve()
	{
		return null;
	}

	@Override
	public PsiDocComment getDocComment()
	{
		return null;
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Override
	public boolean hasTypeParameters()
	{
		return false;
	}

	@Override
	public PsiTypeParameterList getTypeParameterList()
	{
		return null;
	}

	@NotNull
	@Override
	public PsiTypeParameter[] getTypeParameters()
	{
		return new PsiTypeParameter[0];
	}

	@Override
	public PsiClass getContainingClass()
	{
		return null;
	}

	@Override
	public void navigate(boolean requestFocus)
	{

	}

	@Override
	public boolean canNavigate()
	{
		return false;
	}

	@Override
	public boolean canNavigateToSource()
	{
		return false;
	}

	@NotNull
	@Override
	public Project getProject() throws PsiInvalidElementAccessException
	{
		return null;
	}

	@NotNull
	@Override
	public Language getLanguage()
	{
		return null;
	}

	@Override
	public PsiManager getManager()
	{
		return null;
	}

	@NotNull
	@Override
	public PsiElement[] getChildren()
	{
		return new PsiElement[0];
	}

	@Override
	public PsiElement getParent()
	{
		return null;
	}

	@Override
	public PsiElement getFirstChild()
	{
		return null;
	}

	@Override
	public PsiElement getLastChild()
	{
		return null;
	}

	@Override
	public PsiElement getNextSibling()
	{
		return null;
	}

	@Override
	public PsiElement getPrevSibling()
	{
		return null;
	}

	@Override
	public PsiFile getContainingFile() throws PsiInvalidElementAccessException
	{
		return null;
	}

	@Override
	public TextRange getTextRange()
	{
		return null;
	}

	@Override
	public int getStartOffsetInParent()
	{
		return 0;
	}

	@Override
	public int getTextLength()
	{
		return 0;
	}

	@Override
	public PsiElement findElementAt(int offset)
	{
		return null;
	}

	@Override
	public PsiReference findReferenceAt(int offset)
	{
		return null;
	}

	@Override
	public int getTextOffset()
	{
		return 0;
	}

	@Override
	public String getText()
	{
		return null;
	}

	@NotNull
	@Override
	public char[] textToCharArray()
	{
		return new char[0];
	}

	@Override
	public PsiElement getNavigationElement()
	{
		return null;
	}

	@Override
	public PsiElement getOriginalElement()
	{
		return null;
	}

	@Override
	public boolean textMatches(@NotNull @NonNls CharSequence text)
	{
		return false;
	}

	@Override
	public boolean textMatches(@NotNull PsiElement element)
	{
		return false;
	}

	@Override
	public boolean textContains(char c)
	{
		return false;
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{

	}

	@Override
	public void acceptChildren(@NotNull PsiElementVisitor visitor)
	{

	}

	@Override
	public PsiElement copy()
	{
		return null;
	}

	@Override
	public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException
	{

	}

	@Override
	public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public void delete() throws IncorrectOperationException
	{

	}

	@Override
	public void checkDelete() throws IncorrectOperationException
	{

	}

	@Override
	public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException
	{

	}

	@Override
	public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public boolean isValid()
	{
		return false;
	}

	@Override
	public boolean isWritable()
	{
		return false;
	}

	@Override
	public PsiReference getReference()
	{
		return null;
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return new PsiReference[0];
	}

	@Override
	public <T> T getCopyableUserData(Key<T> key)
	{
		return null;
	}

	@Override
	public <T> void putCopyableUserData(Key<T> key, @Nullable T value)
	{

	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place)
	{
		return false;
	}

	@Override
	public PsiElement getContext()
	{
		return null;
	}

	@Override
	public boolean isPhysical()
	{
		return false;
	}

	@NotNull
	@Override
	public GlobalSearchScope getResolveScope()
	{
		return null;
	}

	@NotNull
	@Override
	public SearchScope getUseScope()
	{
		return null;
	}

	@Override
	public ASTNode getNode()
	{
		return null;
	}

	@Override
	public boolean isEquivalentTo(PsiElement another)
	{
		return false;
	}

	@Override
	public Icon getIcon(int flags)
	{
		return null;
	}

	@Override
	public <T> T getUserData(@NotNull Key<T> key)
	{
		return null;
	}

	@Override
	public <T> void putUserData(@NotNull Key<T> key, @Nullable T value)
	{

	}
}
