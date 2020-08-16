package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;

import java.util.function.Supplier;


public interface WModifiableCollection extends WCollection {
	default void add(WAbstractWidget widget) {
		getWidgets().add(widget);

		if (this instanceof WAbstractWidget) {
			widget.onAdded(((WAbstractWidget) this).getInterface(), this);

			((WAbstractWidget) this).onLayoutChange();
		}
	}

	default void remove(WAbstractWidget widget) {
		getWidgets().remove(widget);

		if (this instanceof WAbstractWidget) {
			widget.onRemoved(((WAbstractWidget) this).getInterface(), this);

			((WAbstractWidget) this).onLayoutChange();
		}
	}

	default <W extends WAbstractWidget> W createChild(Supplier<W> factory) {
		return createChild(factory, null, null);
	}

	default <W extends WAbstractWidget> W createChild(Supplier<? extends W> factory, Position position, Size size) {
		W widget = factory.get();
		if (position != null) widget.setPosition(position);
		if (size != null) widget.setSize(size);

		if (this instanceof WAbstractWidget) {
			widget.setInterface(((WAbstractWidget) this).getInterface());
		} else if (this instanceof WInterface) {
			widget.setInterface((WInterface) this);
		}

		if (this instanceof WDrawableElement) {
			widget.setParent((WDrawableElement) this);
		}

		add(widget);

		return widget;
	}

	default <W extends WAbstractWidget> W createChild(Supplier<W> factory, Position position) {
		return createChild(factory, position, null);
	}
}
