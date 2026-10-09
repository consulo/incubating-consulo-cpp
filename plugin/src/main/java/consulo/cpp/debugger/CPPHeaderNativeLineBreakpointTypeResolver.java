package consulo.cpp.debugger;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.CPPHeaderFileType;
import consulo.nativeDev.debugger.NativeLineBreakpointTypeResolver;
import consulo.virtualFileSystem.fileType.FileType;

/**
 * @author VISTALL
 * @since 2026-10-06
 */
@ExtensionImpl
public class CPPHeaderNativeLineBreakpointTypeResolver extends NativeLineBreakpointTypeResolver {
    @Override
    public FileType getFileType() {
        return CPPHeaderFileType.INSTANCE;
    }
}
