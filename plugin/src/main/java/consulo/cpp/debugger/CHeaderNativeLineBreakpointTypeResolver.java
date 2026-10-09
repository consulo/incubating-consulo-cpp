package consulo.cpp.debugger;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.CHeaderFileType;
import consulo.nativeDev.debugger.NativeLineBreakpointTypeResolver;
import consulo.virtualFileSystem.fileType.FileType;

/**
 * @author VISTALL
 * @since 2026-10-06
 */
@ExtensionImpl
public class CHeaderNativeLineBreakpointTypeResolver extends NativeLineBreakpointTypeResolver {
    @Override
    public FileType getFileType() {
        return CHeaderFileType.INSTANCE;
    }
}
