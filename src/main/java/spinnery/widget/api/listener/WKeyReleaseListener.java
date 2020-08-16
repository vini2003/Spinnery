package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;


 /
public interface WKeyReleaseListener<W extends WAbstractWidget> {
	void event(W widget, int keyReleased, int character, int keyModifier);
}
