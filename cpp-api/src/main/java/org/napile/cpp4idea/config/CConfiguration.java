package org.napile.cpp4idea.config;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * @author VISTALL
 * @date 2:14/02.07.2012
 */
public class CConfiguration
{
	public static final List<String> NONE = Arrays.asList("MY_VARIABLE");

	public static boolean isDefined(@Nullable String var)
	{
		return var != null && NONE.contains(var.trim());
	}
}
