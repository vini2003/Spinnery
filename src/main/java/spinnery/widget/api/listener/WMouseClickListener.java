package spinnery.widget.api.listener;

import spinnery.widget.WAbstractWidget;

public interface WMouseClickListener<W extends WAbstractWidget> {
	void event(W widget, int mouseX, int mouseY, int mouseButton);
}
