package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a widget gains focus.
 */
public interface WFocusGainListener<W extends WAbstractWidget> {
	void event(W widget);
}
