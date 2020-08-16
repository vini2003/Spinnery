package spinnery.widget.declaration.size;

import spinnery.common.utilities.miscellaneous.Size;

public interface WViewported {
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
