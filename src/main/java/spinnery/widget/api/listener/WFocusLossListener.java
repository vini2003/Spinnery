package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a widget loses focus.
 */
public interface WFocusLossListener<W extends WAbstractWidget> {
	void event(W widget);
}
