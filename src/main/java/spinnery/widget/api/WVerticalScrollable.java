package spinnery.widget.api;


 /
public interface WVerticalScrollable extends WScrollable {

	  the first widgets should be visible, and the scrollable is considered to be scrolled to the start.
	 *

	float getStartAnchorY();


	  the last widgets should be visible, and the scrollable is considered to be scrolled to the end.
	 *

	float getEndAnchorY();


	  the Y axis the contained widgets have been scrolled.
	 *

	float getStartOffsetY();
}
