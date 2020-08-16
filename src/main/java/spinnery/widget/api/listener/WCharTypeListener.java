package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;


 /
public interface WCharTypeListener<W extends WAbstractWidget> {
	void event(W widget, char character, int keyCode);
}
