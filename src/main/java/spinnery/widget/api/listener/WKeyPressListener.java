package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WKeyPressListener<W extends WWidget> {
    void event(W widget, int keyPressed, int character, int keyModifier);
}
