package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WTooltipDrawListener<W extends WWidget> {
    void event(W widget, int mouseX, int mouseY);
}
