package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a character is typed.
 */
public interface WCharTypeListener<W extends WAbstractWidget> {
	void event(W widget, char character, int keyCode);
}
