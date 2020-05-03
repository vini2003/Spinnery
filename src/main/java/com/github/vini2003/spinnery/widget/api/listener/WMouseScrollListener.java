package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when the mouse's scroll wheel is scrolled.
 */
public interface WMouseScrollListener<W extends WAbstractWidget> {
	void event(W widget, int mouseX, int mouseY, double deltaY);
}
