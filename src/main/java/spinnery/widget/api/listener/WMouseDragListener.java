package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

public interface WMouseDragListener<W extends WAbstractWidget> {
    void event(W widget, int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY);
}
