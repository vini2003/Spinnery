package spinnery.widget.api.listener;

import spinnery.widget.WWidget;

public interface WCharTypeListener<W extends WWidget> {
    void event(W widget, char character, int keyCode);
}
