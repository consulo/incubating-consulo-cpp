package consulo.cpp.debugger;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.CSourceFileType;
import consulo.nativeDev.debugger.NativeLineBreakpointTypeResolver;
import consulo.virtualFileSystem.fileType.FileType;

/**
 * @author VISTALL
 * @since 2026-10-06
 */
@ExtensionImpl
public class CSourceNativeLineBreakpointTypeResolver extends NativeLineBreakpointTypeResolver {
    @Override
    public FileType getFileType() {
        return CSourceFileType.INSTANCE;
    }
}
