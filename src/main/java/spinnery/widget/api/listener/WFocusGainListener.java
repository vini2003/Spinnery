package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when a widget gains focus.
 */
public interface WFocusGainListener<W extends WAbstractWidget> {
	void event(W widget);
}
