package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;


 /
public interface WMouseDragListener<W extends WAbstractWidget> {
	void event(W widget, float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY);
}
