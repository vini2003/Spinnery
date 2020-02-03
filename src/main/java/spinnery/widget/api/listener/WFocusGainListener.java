package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WFocusGainListener<W extends WWidget> {
    void event(W widget);
}
