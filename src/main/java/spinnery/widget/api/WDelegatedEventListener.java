package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

import java.util.Collection;

public interface WDelegatedEventListener {
	Collection<? extends WAbstractWidget> getDelegatedWidgets();
}
