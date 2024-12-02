/**
 * @author VISTALL
 * @since 15/01/2023
 */
module consulo.cpp
{
	requires consulo.cpp.api;

	// try to fix issue with text attributes search
	opens consulo.cpp.impl;
}