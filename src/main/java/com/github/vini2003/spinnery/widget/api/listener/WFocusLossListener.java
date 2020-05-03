package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a widget loses focus.
 */
public interface WFocusLossListener<W extends WAbstractWidget> {
	void event(W widget);
}
