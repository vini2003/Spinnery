package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WFocusLossListener<W extends WWidget> {
    void event(W widget);
}
