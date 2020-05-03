package com.github.vini2003.spinnery.widget.api;

/**
 * Generic interface representing a container that may be scrolled horizontally.
 */
public interface WHorizontalScrollable extends WScrollable {
	/**
	 * Gets the X coordinate of the start anchor. When containing widgets are positioned at the start anchor,
	 * the first widgets should be visible, and the scrollable is considered to be scrolled to the start.
	 *
	 * @return start anchor X position
	 */
	int getStartAnchorX();

	/**
	 * Gets the X coordinate of the end anchor. When containing widgets are positioned at the end anchor,
	 * the last widgets should be visible, and the scrollable is considered to be scrolled to the end.
	 *
	 * @return end anchor X position
	 */
	int getEndAnchorX();

	/**
	 * Gets the X offset from the start anchor. This offset represents how far from the start anchor along
	 * the X axis the contained widgets have been scrolled.
	 *
	 * @return X offset from start anchor
	 */
	int getStartOffsetX();
}
