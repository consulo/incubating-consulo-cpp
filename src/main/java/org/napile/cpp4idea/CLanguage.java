package org.napile.cpp4idea;

import com.intellij.lang.Language;

/**
 * @author VISTALL
 * @date 0:57/10.12.2011
 */
public class CLanguage extends Language
{
	public static final Language INSTANCE = new CLanguage();

	public CLanguage()
	{
		super("C");
	}

	@Override
	public String getDisplayName()
	{
		return "C/C++";
	}

	@Override
	public boolean isCaseSensitive()
	{
		return true;
	}
}
