package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a key is released.
 */
public interface WKeyReleaseListener<W extends WAbstractWidget> {
	void event(W widget, int keyReleased, int character, int keyModifier);
}
