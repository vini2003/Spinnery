package spinnery.widget.api;

/**
 * Generic interface representing a layout element.
 *
 * <p>A layout element is defined as an object that must have a position and size, and may be drawn onto the screen.
 * Every widget must implement this interface
 */
public interface WLayoutElement extends WPositioned, WSized, Comparable<WLayoutElement> {
	void draw();

	/**
	 * Runs whenever the layout has been changed significantly enough to warrant potential adjustment of
	 * this layout element. Such layout changes include e.g. changes in position and size of this element, or
	 * realignment of the parent element. Other widgets may call this method when they change their layout
	 * in other ways, e.g. adding or removing a label.
	 */
	default void onLayoutChange() {
	}

	@Override
	default int compareTo(WLayoutElement element) {
		return Integer.compare(element.getZ(), getZ());
	}
}
