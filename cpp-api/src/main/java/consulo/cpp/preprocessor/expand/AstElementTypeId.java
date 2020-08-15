package consulo.cpp.preprocessor.expand;

import com.intellij.psi.tree.IElementType;

import java.util.Objects;

/**
 * @author VISTALL
 * @since 18:00/2020-08-15
 */
public final class AstElementTypeId {
	private final int myStartOffset;
	private final IElementType myElementType;

	public AstElementTypeId(int startOffset, IElementType elementType) {
		myStartOffset = startOffset;
		myElementType = elementType;
	}

	public int getStartOffset() {
		return myStartOffset;
	}

	public IElementType getElementType() {
		return myElementType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AstElementTypeId that = (AstElementTypeId) o;
		return myStartOffset == that.myStartOffset &&
				Objects.equals(myElementType, that.myElementType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myStartOffset, myElementType);
	}

	@Override
	public String toString() {
		return "AstElementTypeId{" +
				"myStartOffset=" + myStartOffset +
				", myElementType=" + myElementType +
				'}';
	}
}
