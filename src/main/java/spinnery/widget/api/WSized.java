package spinnery.widget.api;

/**
 * Generic interface representing an object that may have a width and height. Utility classes are well-served by this
 * interface; other use cases should probably implement a less generic interface, such as {@link WLayoutElement}.
 */
public interface WSized {
	/**
	 * Retrieves the width of this object.
	 * @return The width of this object.
	 */
	default int getWidth() {
		return 0;
	}

	/**
	 * Retrieves the height of this object.
	 * @return The height of this object.
	 */
	default int getHeight() {
		return 0;
	}
}
