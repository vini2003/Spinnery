package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

public interface WFocusLossListener<W extends WAbstractWidget> {
    void event(W widget);
}
