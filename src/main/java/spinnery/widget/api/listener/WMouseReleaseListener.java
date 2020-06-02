package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a mouse button is released.
 */
public interface WMouseReleaseListener<W extends WAbstractWidget> {
	void event(W widget, float mouseX, float mouseY, int mouseButton);
}
