package spinnery.widget.declaration.scroll;

import spinnery.widget.declaration.size.WViewported;

public interface WScrolled extends WViewported {
	void scroll(double deltaX, double deltaY);
}
