/*
 * Copyright 2010-2013 napile.org
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

import java.io.IOException;

import org.napile.cpp4idea.config.facet.CDialectProvider;
import org.napile.cpp4idea.core.CoreCDialectProvider;
import org.napile.cpp4idea.ide.debugToolWindow.psiDebug.XmlPsiDebugger;
import org.napile.cpp4idea.lang.CDialect;
import org.napile.cpp4idea.lang.parser.CParserDefinitionImpl;
import org.napile.cpp4idea.lang.preprocessor.CPreprocessor;
import org.napile.cpp4idea.lang.psi.CPsiFile;
import org.napile.cpp4idea.lang.psiInitial.CPsiSharpFile;
import com.intellij.core.CoreApplicationEnvironment;
import com.intellij.core.CoreProjectEnvironment;
import com.intellij.core.CoreProjectLoader;
import com.intellij.core.ProjectModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.UsefulTestCase;

/**
 * @author VISTALL
 * @date 13:25/08.02.13
 */
public abstract class CppTreeTestCase extends UsefulTestCase
{
	public abstract String getProjectName();

	public abstract CDialect getDialect();

	@Override
	protected void runTest() throws Throwable
	{
		String fileName = getTestName(true);
		fileName = fileName.replace("$", ".");

		CoreApplicationEnvironment appEnv = new CoreApplicationEnvironment(getTestRootDisposable());
		appEnv.registerFileType(CFileType.INSTANCE, "h");
		appEnv.registerFileType(CFileType.INSTANCE, "c");
		appEnv.registerParserDefinition(new CParserDefinitionImpl());
		appEnv.getApplication().registerService(CDialectProvider.class, new CoreCDialectProvider(getDialect()));
		new ProjectModel.InitApplicationEnvironment(appEnv);

		CoreProjectEnvironment prjEnv = new CoreProjectEnvironment(getTestRootDisposable(), appEnv);
		new ProjectModel.InitProjectEnvironment(prjEnv);

		final String projectPath = "cpp4idea.api/src/test/" + getProjectName();
		VirtualFile vFile = StandardFileSystems.local().findFileByPath(projectPath);
		CoreProjectLoader.loadProject(prjEnv.getProject(), vFile);
		final ModuleManager moduleManager = ModuleManager.getInstance(prjEnv.getProject());
		final Module[] modules = moduleManager.getModules();

		//CoreModule module = (CoreModule) modules[0];

		VirtualFile targetFile = StandardFileSystems.local().findFileByPath(projectPath + "/src/" + fileName);

		CPsiSharpFile psiFile = (CPsiSharpFile) PsiManager.getInstance(prjEnv.getProject()).findFile(targetFile);

		XmlPsiDebugger x = new XmlPsiDebugger();
		assertEquals(x.toText(psiFile), textOf(projectPath + "/src/" + fileName + ".before"));

		CPsiFile processedFile = CPreprocessor.preProcess(psiFile);

		assertEquals(x.toText(processedFile), textOf(projectPath + "/src/" + fileName + ".after"));
	}

	private String textOf(String path)
	{
		try
		{
			VirtualFile virtualFile = StandardFileSystems.local().findFileByPath(path);
			return virtualFile == null ? "" : new String(virtualFile.contentsToByteArray());
		}
		catch(IOException e)
		{
			return e.getMessage();
		}
	}

	@Override
	protected boolean shouldContainTempFiles()
	{
		return false;
	}
}
