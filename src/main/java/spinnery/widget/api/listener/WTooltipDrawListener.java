package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when widget's tooltip should be rendered.
 */
public interface WTooltipDrawListener<W extends WAbstractWidget> {
    void event(W widget, float mouseX, float mouseY);
}
