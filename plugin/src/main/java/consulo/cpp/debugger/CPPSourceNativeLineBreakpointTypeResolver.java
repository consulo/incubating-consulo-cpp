package consulo.cpp.debugger;

import consulo.annotation.component.ExtensionImpl;
import consulo.cpp.lang.CPPSourceFileType;
import consulo.nativeDev.debugger.NativeLineBreakpointTypeResolver;
import consulo.virtualFileSystem.fileType.FileType;

/**
 * @author VISTALL
 * @since 2026-10-06
 */
@ExtensionImpl
public class CPPSourceNativeLineBreakpointTypeResolver extends NativeLineBreakpointTypeResolver {
    @Override
    public FileType getFileType() {
        return CPPSourceFileType.INSTANCE;
    }
}
