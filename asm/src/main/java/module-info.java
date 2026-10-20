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

@NullMarked
module consulo.cpp.asm {
    requires consulo.cpp.api;
    requires consulo.asm.language.api;
    requires consulo.asm.gas.impl;
    requires consulo.asm.nasm.impl;
    requires consulo.annotation;
    requires consulo.language.api;
    requires consulo.module.api;
    requires consulo.module.content.api;
    requires consulo.platform.api;
    requires consulo.virtual.file.system.api;

    exports consulo.cpp.asm;
}
