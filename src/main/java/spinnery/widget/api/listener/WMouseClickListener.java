package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WMouseClickListener<W extends WWidget> {
    void event(W widget, int mouseX, int mouseY, int mouseButton);
}
