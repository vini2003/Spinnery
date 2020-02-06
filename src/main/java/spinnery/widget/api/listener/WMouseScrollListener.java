package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

public interface WMouseScrollListener<W extends WAbstractWidget> {
    void event(W widget, int mouseX, int mouseY, double deltaY);
}
