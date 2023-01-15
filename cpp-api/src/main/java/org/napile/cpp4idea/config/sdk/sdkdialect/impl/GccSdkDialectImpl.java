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

package org.napile.cpp4idea.config.sdk.sdkdialect.impl;

import org.napile.cpp4idea.config.sdk.sdkdialect.SdkDialect;

/**
 * @author VISTALL
 * @date 16:45/12.12.2011
 */
public class GccSdkDialectImpl extends SdkDialect
{
	public GccSdkDialectImpl(String name)
	{
		super(name);
	}

	@Override
	public boolean isSupported(String path)
	{
		return false;
	}

	//TODO [VISTALL]
}
