package com.github.vini2003.spinnery.widget.api;

/**
 * Utility interface building on {@link WInnerSized} that enables simple use of a {@link Padding} value to provide
 * the inner anchor and size.
 */
public interface WPadded extends WInnerSized {
	@Override
	default Size getInnerSize() {
		return Size.of(this).add(
				-(getPadding().getLeft() + getPadding().getRight()),
				-(getPadding().getTop() + getPadding().getBottom())
		);
	}

	Padding getPadding();

	@Override
	default Position getInnerAnchor() {
		return Position.of(this, getPadding().getLeft(), getPadding().getTop());
	}
}
