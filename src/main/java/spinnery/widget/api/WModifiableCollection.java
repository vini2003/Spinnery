package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;
import spinnery.widget.WWidgetFactory;

public interface WModifiableCollection extends WCollection {
	void add(WAbstractWidget... widgets);

	void remove(WAbstractWidget... widgets);

	default WWidgetFactory getFactory() {
		return new WWidgetFactory(this);
	}

	default <W extends WAbstractWidget> W createChild(Class<W> wClass) {
		return createChild(wClass, null, null);
	}

	default <W extends WAbstractWidget> W createChild(Class<W> wClass, WPosition position) {
		return createChild(wClass, position, null);
	}

	default <W extends WAbstractWidget> W createChild(Class<W> wClass, WPosition position, WSize size) {
		W widget = getFactory().build(wClass, position, size);
		if (this instanceof WLayoutElement) widget.setParent((WLayoutElement) this);
		add(widget);
		return widget;
	}
}
