package spinnery.widget.api;

import spinnery.widget.WWidgetFactory;
import spinnery.widget.WWidget;

public interface WModifiableCollection extends WCollection {
	void add(WWidget... widgets);

	void remove(WWidget... widgets);

	default WWidgetFactory getFactory() {
		return new WWidgetFactory(this);
	}

	default <T extends WWidget> T createChild(Class<T> wClass) {
		return createChild(wClass, null, null);
	}

	default <T extends WWidget> T createChild(Class<T> wClass, WPosition position) {
		return createChild(wClass, position, null);
	}

	default <T extends WWidget> T createChild(Class<T> wClass, WPosition position, WSize size) {
		T widget = getFactory().build(wClass, position, size);
		if (this instanceof WLayoutElement) widget.setParent((WLayoutElement) this);
		add(widget);
		return widget;
	}
}
