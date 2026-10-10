/*
 * Copyright 2013-2026 consulo.io
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

import org.jspecify.annotations.NullMarked;

/**
 * @author VISTALL
 * @since 2026-10-10
 */
@NullMarked
module consulo.cpp.msbuild {
    requires consulo.cpp.api;
    requires consulo.msbuild.api;
    requires consulo.nativeDev.debugger.api;
    requires consulo.nativeDev.debugger.dap;
    requires consulo.annotation;
    requires consulo.application.api;
    requires consulo.component.api;
    requires consulo.disposer.api;
    requires consulo.execution.api;
    requires consulo.localize.api;
    requires consulo.logging.api;
    requires consulo.module.api;
    requires consulo.module.content.api;
    requires consulo.platform.api;
    requires consulo.process.api;
    requires consulo.project.api;
    requires consulo.ui.api;
    requires consulo.ui.ex.api;
    requires consulo.util.io;
    requires consulo.util.jdom;
    requires consulo.util.lang;
    requires consulo.util.xml.serializer;
    requires consulo.virtual.file.system.api;
    requires consulo.base.icon.library;
    requires org.jdom;

    exports consulo.cpp.msbuild;
    exports consulo.cpp.msbuild.module;
    exports consulo.cpp.msbuild.run;
}
