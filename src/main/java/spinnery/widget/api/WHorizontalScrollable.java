package spinnery.widget.api;


 /
public interface WHorizontalScrollable extends WScrollable {

	  the first widgets should be visible, and the scrollable is considered to be scrolled to the start.
	 *

	float getStartAnchorX();


	  the last widgets should be visible, and the scrollable is considered to be scrolled to the end.
	 *

	float getEndAnchorX();


	  the X axis the contained widgets have been scrolled.
	 *

	float getStartOffsetX();
}
