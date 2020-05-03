package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a widget has to be aligned.
 */
public interface WAlignListener<W extends WAbstractWidget> {
	void event(W widget);
}
