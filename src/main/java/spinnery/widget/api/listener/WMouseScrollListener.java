package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WMouseScrollListener<W extends WWidget> {
    void event(W widget, int mouseX, int mouseY, double deltaY);
}
