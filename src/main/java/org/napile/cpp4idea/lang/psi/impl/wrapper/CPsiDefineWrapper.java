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
 * limitations under the License.
 */

package org.napile.cpp4idea.lang.psi.impl.wrapper;

import javax.swing.Icon;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;

/**
 * @author VISTALL
 * @date 17:48/16.12.2011
 */
public class CPsiDefineWrapper implements PsiElement
{
	private final PsiElement _target;
	private final boolean _defined;

	public CPsiDefineWrapper(PsiElement target, boolean def)
	{
		_target = target;
		_defined = def;
	}

	@NotNull
	@Override
	public Project getProject() throws PsiInvalidElementAccessException
	{
		return _target.getProject();
	}

	@NotNull
	@Override
	public Language getLanguage()
	{
		return _target.getLanguage();
	}

	@Override
	public PsiManager getManager()
	{
		return _target.getManager();
	}

	@NotNull
	@Override
	public PsiElement[] getChildren()
	{
		return _target.getChildren();
	}

	@Override
	public PsiElement getParent()
	{
		return _target.getParent();
	}

	@Override
	public PsiElement getFirstChild()
	{
		return _target.getFirstChild();
	}

	@Override
	public PsiElement getLastChild()
	{
		return _target.getLastChild();
	}

	@Override
	public PsiElement getNextSibling()
	{
		return _target.getNextSibling();
	}

	@Override
	public PsiElement getPrevSibling()
	{
		return _target.getPrevSibling();
	}

	@Override
	public PsiFile getContainingFile() throws PsiInvalidElementAccessException
	{
		return _target.getContainingFile();
	}

	@Override
	public TextRange getTextRange()
	{
		return _target.getTextRange();
	}

	@Override
	public int getStartOffsetInParent()
	{
		return _target.getStartOffsetInParent();
	}

	@Override
	public int getTextLength()
	{
		return _target.getTextLength();
	}

	@Override
	public PsiElement findElementAt(int offset)
	{
		return _target.findElementAt(offset);
	}

	@Override
	public PsiReference findReferenceAt(int offset)
	{
		return _target.findReferenceAt(offset);
	}

	@Override
	public int getTextOffset()
	{
		return _target.getTextOffset();
	}

	@Override
	public String getText()
	{
		return _target.getText();
	}

	@NotNull
	@Override
	public char[] textToCharArray()
	{
		return _target.textToCharArray();
	}

	@Override
	public PsiElement getNavigationElement()
	{
		return _target.getNavigationElement();
	}

	@Override
	public PsiElement getOriginalElement()
	{
		return _target.getOriginalElement();
	}

	@Override
	public boolean textMatches(@NotNull @NonNls CharSequence text)
	{
		return _target.textMatches(text);
	}

	@Override
	public boolean textMatches(@NotNull PsiElement element)
	{
		return _target.textMatches(element);
	}

	@Override
	public boolean textContains(char c)
	{
		return _target.textContains(c);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		visitor.visitElement(this);
	}

	@Override
	public void acceptChildren(@NotNull PsiElementVisitor visitor)
	{
		//
	}

	@Override
	public PsiElement copy()
	{
		return _target.copy();
	}

	@Override
	public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return _target.add(element);
	}

	@Override
	public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException
	{
		return _target.addBefore(element, anchor);
	}

	@Override
	public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException
	{
		return _target.addAfter(element, anchor);
	}

	@Override
	public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException
	{
		_target.checkAdd(element);
	}

	@Override
	public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException
	{
		return _target.addRange(first, last);
	}

	@Override
	public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException
	{
		return _target.addRangeBefore(first, last, anchor);
	}

	@Override
	public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException
	{
		return _target.addRangeAfter(first, last, anchor);
	}

	@Override
	public void delete() throws IncorrectOperationException
	{
		_target.delete();
	}

	@Override
	public void checkDelete() throws IncorrectOperationException
	{
		_target.checkDelete();
	}

	@Override
	public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException
	{
		_target.deleteChildRange(first, last);
	}

	@Override
	public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException
	{
		return _target.replace(newElement);
	}

	@Override
	public boolean isValid()
	{
		return _target.isValid();
	}

	@Override
	public boolean isWritable()
	{
		return _target.isWritable();
	}

	@Override
	public PsiReference getReference()
	{
		return _target.getReference();
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return _target.getReferences();
	}

	@Override
	public <T> T getCopyableUserData(Key<T> key)
	{
		return _target.getCopyableUserData(key);
	}

	@Override
	public <T> void putCopyableUserData(Key<T> key, @Nullable T value)
	{
		_target.putCopyableUserData(key, value);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place)
	{
		return _target.processDeclarations(processor, state, lastParent, place);
	}

	@Override
	public PsiElement getContext()
	{
		return _target.getContext();
	}

	@Override
	public boolean isPhysical()
	{
		return _target.isPhysical();
	}

	@NotNull
	@Override
	public GlobalSearchScope getResolveScope()
	{
		return _target.getResolveScope();
	}

	@NotNull
	@Override
	public SearchScope getUseScope()
	{
		return _target.getUseScope();
	}

	@Override
	public ASTNode getNode()
	{
		return _target.getNode();
	}

	@Override
	public boolean isEquivalentTo(PsiElement another)
	{
		return _target.isEquivalentTo(another);
	}

	@Override
	public Icon getIcon(int flags)
	{
		return _target.getIcon(flags);
	}

	@Override
	public <T> T getUserData(@NotNull Key<T> key)
	{
		return _target.getUserData(key);
	}

	@Override
	public <T> void putUserData(@NotNull Key<T> key, @Nullable T value)
	{
		_target.putUserData(key, value);
	}

	public boolean isDefined()
	{
		return _defined;
	}

	public PsiElement getTarget()
	{
		return _target;
	}
}
