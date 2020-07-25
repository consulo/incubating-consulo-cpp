/*
 * Copyright 2010-2012 napile.org
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

package org.napile.cpp4idea.ide.debugToolWindow;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.napile.cpp4idea.ide.debugToolWindow.psiDebug.XmlPsiDebugger;
import consulo.cpp.preprocessor.psi.CPsiSharpFile;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.Alarm;

/**
 * @author VISTALL
 * @date 15:54/29.12.12
 */
public abstract class AbstractDebugPsiTreeToolWindow extends JPanel implements Disposable {
	private static final int UPDATE_DELAY = 500;
	protected static final String DEFAULT_TEXT =
			"<!--\n" +
					"No opened file\n" +
					"-->";

	private final Editor editor;
	private final Alarm alarm;
	private final Project project;
	private Location currentLocation;

	protected XmlPsiDebugger debugger = new XmlPsiDebugger();

	public AbstractDebugPsiTreeToolWindow(final Project project) {
		super(new BorderLayout());
		this.project = project;
		editor = EditorFactory.getInstance().createEditor(EditorFactory.getInstance().createDocument(""), project, FileTypeManager.getInstance().getStdFileType("XML"), true);
		add(editor.getComponent());
		alarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD, this);
		alarm.addRequest(new Runnable() {
			@Override
			public void run() {
				alarm.addRequest(this, UPDATE_DELAY);
				Location location = Location.fromEditor(FileEditorManager.getInstance(project).getSelectedTextEditor());
				if (!Comparing.equal(location, currentLocation)) {
					update(location, currentLocation);
					currentLocation = location;
				}

			}
		}, UPDATE_DELAY);
	}

	private void update(Location location, Location oldLocation) {
		Editor editor = location.editor;

		if (editor == null) {
			setText(DEFAULT_TEXT);
		} else {
			VirtualFile vFile = ((EditorEx) editor).getVirtualFile();
			if (vFile == null) {
				setText(DEFAULT_TEXT);
				return;
			}

			PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);
			if (!(psiFile instanceof CPsiSharpFile)) {
				setText(DEFAULT_TEXT);
				return;
			}

			if (oldLocation == null || !Comparing.equal(oldLocation.editor, location.editor) || oldLocation.modificationStamp != location.modificationStamp) {
				setText(toText(psiFile));
			}
		}
	}

	protected abstract String toText(PsiFile psiFile);

	protected void setText(final String text) {
		new WriteCommandAction(project) {
			@Override
			protected void run(Result result) throws Throwable {
				editor.getDocument().setText(text);
			}
		}.execute();
	}

	@Override
	public void dispose() {
		EditorFactory.getInstance().releaseEditor(editor);
	}

	public static class Location {
		final Editor editor;
		final long modificationStamp;
		final int startOffset;
		final int endOffset;

		private Location(Editor editor) {
			this.editor = editor;
			modificationStamp = editor != null ? editor.getDocument().getModificationStamp() : 0;
			startOffset = editor != null ? editor.getSelectionModel().getSelectionStart() : 0;
			endOffset = editor != null ? editor.getSelectionModel().getSelectionEnd() : 0;
		}

		public static Location fromEditor(Editor editor) {
			return new Location(editor);
		}

		public Editor getEditor() {
			return editor;
		}

		public long getModificationStamp() {
			return modificationStamp;
		}

		public int getStartOffset() {
			return startOffset;
		}

		public int getEndOffset() {
			return endOffset;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Location)) {
				return false;
			}

			Location location = (Location) o;

			if (endOffset != location.endOffset) {
				return false;
			}
			if (modificationStamp != location.modificationStamp) {
				return false;
			}
			if (startOffset != location.startOffset) {
				return false;
			}
			if (editor != null ? !editor.equals(location.editor) : location.editor != null) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = editor != null ? editor.hashCode() : 0;
			result = 31 * result + (int) (modificationStamp ^ (modificationStamp >>> 32));
			result = 31 * result + startOffset;
			result = 31 * result + endOffset;
			return result;
		}
	}
}
