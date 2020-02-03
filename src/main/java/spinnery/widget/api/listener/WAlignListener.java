package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WAlignListener<W extends WWidget> {
    void event(W widget);
}
