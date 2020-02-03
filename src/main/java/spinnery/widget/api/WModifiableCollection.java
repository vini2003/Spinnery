package spinnery.widget.api;

import spinnery.debug.WWidgetFactory;
import spinnery.widget.WWidget;

public interface WModifiableCollection extends WCollection {
	void add(WWidget... widgets);

	void remove(WWidget... widgets);

	default WWidgetFactory getFactory() {
		return new WWidgetFactory(this);
	}
}
