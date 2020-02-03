package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WKeyReleaseListener<W extends WWidget> {
    void event(W widget, int keyReleased, int character, int keyModifier);
}
