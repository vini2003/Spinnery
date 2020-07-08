package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

/**
 * An interface for events called when the mouse is moved.
 */
public interface WMouseMoveListener<W extends WAbstractWidget> {
    void event(W widget, float mouseX, float mouseY);
}
