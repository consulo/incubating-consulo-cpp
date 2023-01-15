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

package org.napile.cpp4idea.config.sdk.sdkdialect;

import org.napile.cpp4idea.config.sdk.sdkdialect.impl.MsVs2010SdkDialectImpl;

/**
 * @author VISTALL
 * @date 16:40/12.12.2011
 */
public abstract class SdkDialect
{
	public static final SdkDialect[] DIALECTS;

	static
	{
		DIALECTS = new SdkDialect[1];

		DIALECTS[0] = new MsVs2010SdkDialectImpl();
	}

	private String _name;

	protected SdkDialect(String name)
	{
		_name = name;
	}

	public abstract boolean isSupported(String path);

	@Override
	public String toString()
	{
		return _name;
	}

	public String getName()
	{
		return _name;
	}
}
