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

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;
import consulo.cpp.lang.CHeaderFileType;
import consulo.cpp.lang.CPPHeaderFileType;
import consulo.cpp.lang.CPPSourceFileType;
import consulo.cpp.lang.CSourceFileType;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @date 1:03/10.12.2011
 */
@ExtensionImpl
public class CFileTypeFactory extends FileTypeFactory
{
	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer consumer)
	{
		consumer.consume(CSourceFileType.INSTANCE);
		consumer.consume(CPPSourceFileType.INSTANCE);

		consumer.consume(CHeaderFileType.INSTANCE);
		consumer.consume(CPPHeaderFileType.INSTANCE);
	}
}
