package spinnery.widget.api;

/**
 * Generic interface representing a "viewport", i.e. an object that has a larger underlying area, of which only
 * a part is visible.
 */
public interface WViewport {
	default int getUnderlyingHeight() {
		return getUnderlyingSize().getHeight();
	}

	Size getUnderlyingSize();

	default int getUnderlyingWidth() {
		return getUnderlyingSize().getWidth();
	}

	default int getVisibleHeight() {
		return getVisibleSize().getHeight();
	}

	Size getVisibleSize();

	default int getVisibleWidth() {
		return getVisibleSize().getWidth();
	}
}
