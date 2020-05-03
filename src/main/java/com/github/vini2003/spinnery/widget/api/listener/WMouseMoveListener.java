package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when the mouse is moved.
 */
public interface WMouseMoveListener<W extends WAbstractWidget> {
	void event(W widget, int mouseX, int mouseY);
}
