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

package org.napile.cpp4idea;

import java.lang.ref.Reference;
import java.util.ResourceBundle;

import consulo.application.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;
import consulo.util.lang.ref.SoftReference;

/**
 * @author VISTALL
 * @date 19:39/13.12.2011
 */
public class CBundle {
	private static Reference<ResourceBundle> BUNDLE;

	@NonNls
	public static final String PATH_TO_BUNDLE = "org.napile.cpp4idea.CBundle";

	private CBundle() {
		//
	}

	public static String message(@PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key) {
		return CommonBundle.message(getBundle(), key);
	}

	public static String message(@PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key, Object... params) {
		return CommonBundle.message(getBundle(), key, params);
	}

	private static ResourceBundle getBundle() {
		ResourceBundle bundle = null;
		if (BUNDLE != null) {
			bundle = BUNDLE.get();
		}

		if (bundle == null) {
			bundle = ResourceBundle.getBundle(PATH_TO_BUNDLE);
			BUNDLE = new SoftReference<ResourceBundle>(bundle);
		}
		return bundle;
	}
}
