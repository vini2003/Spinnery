package spinnery.widget.declaration.size;

import spinnery.common.utilities.miscellaneous.Padding;
import spinnery.common.utilities.miscellaneous.Position;
import spinnery.common.utilities.miscellaneous.Size;
import spinnery.widget.declaration.position.WPositioned;

public interface WPadded extends WPositioned, WSized {
	default Size getInnerSize() {
		return Size.of(this).add(
				-(getPadding().getLeft() + getPadding().getRight()),
				-(getPadding().getTop() + getPadding().getBottom())
		);
	}

	Padding getPadding();

	default Position getInnerAnchor() {
		return Position.of(this, getPadding().getLeft(), getPadding().getTop());
	}
}
