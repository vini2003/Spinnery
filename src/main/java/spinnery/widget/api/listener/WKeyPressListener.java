package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

public interface WKeyPressListener<W extends WAbstractWidget> {
    void event(W widget, int keyPressed, int character, int keyModifier);
}
