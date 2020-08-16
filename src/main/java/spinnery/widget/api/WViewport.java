package spinnery.widget.api;

import spinnery.common.utilities.miscellaneous.Size;

public interface WViewport {
	Size getUnderlyingSize();

	default float getUnderlyingHeight() {
		return getUnderlyingSize().getHeight();
	}

	default float getUnderlyingWidth() {
		return getUnderlyingSize().getWidth();
	}

	Size getVisibleSize();

	default float getVisibleHeight() {
		return getVisibleSize().getHeight();
	}

	default float getVisibleWidth() {
		return getVisibleSize().getWidth();
	}
}
