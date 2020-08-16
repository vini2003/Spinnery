package spinnery.widget.api;

import spinnery.common.utilities.miscellaneous.Position;
import spinnery.common.utilities.miscellaneous.Size;

public interface WInnerSized extends WDrawableElement {
	Size getInnerSize();

	Position getInnerAnchor();
}
