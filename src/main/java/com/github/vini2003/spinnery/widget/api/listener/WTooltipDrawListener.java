package com.github.vini2003.spinnery.widget.api.listener;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when widget's tooltip should be rendered.
 */
public interface WTooltipDrawListener<W extends WAbstractWidget> {
	void event(W widget, int mouseX, int mouseY);
}
