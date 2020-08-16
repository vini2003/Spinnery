package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;


 /
public interface WMouseReleaseListener<W extends WAbstractWidget> {
	void event(W widget, float mouseX, float mouseY, int mouseButton);
}
