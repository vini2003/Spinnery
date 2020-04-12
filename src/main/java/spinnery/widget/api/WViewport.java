package spinnery.widget.api;

/**
 * Generic interface representing a "viewport", i.e. an object that has a larger underlying area, of which only
 * a part is visible.
 */
public interface WViewport {
	/**
	 * Retrieves the underlying size of this object.
	 *
	 * @return The underlying size of this object.
	 */
	Size getUnderlyingSize();

	/**
	 * Retrieves the underlying height of this object.
	 *
	 * @return The underlying height of this object.
	 */
	default int getUnderlyingHeight() {
		return getUnderlyingSize().getHeight();
	}

	/**
	 * Retrieves the underlying width of this object.
	 *
	 * @return The underlying width of this object.
	 */
	default int getUnderlyingWidth() {
		return getUnderlyingSize().getWidth();
	}

	/**
	 * Retrieves the visible size of this object.
	 *
	 * @return The visible size of this object.
	 */
	Size getVisibleSize();

	/**
	 * Retrieves the visible height of this object.
	 *
	 * @return The visible height of this object.
	 */
	default int getVisibleHeight() {
		return getVisibleSize().getHeight();
	}

	/**
	 * Retrieves the visible height of this object.
	 *
	 * @return The visible height of this object.
	 */
	default int getVisibleWidth() {
		return getVisibleSize().getWidth();
	}
}
