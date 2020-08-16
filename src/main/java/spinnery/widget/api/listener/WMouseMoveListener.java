package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;


 /
public interface WMouseMoveListener<W extends WAbstractWidget> {
	void event(W widget, float mouseX, float mouseY);
}
