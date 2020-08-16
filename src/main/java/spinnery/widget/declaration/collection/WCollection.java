package spinnery.widget.declaration.collection;

import spinnery.widget.implementation.WAbstractWidget;

import java.util.ArrayList;

public interface WCollection {
	ArrayList<WAbstractWidget> getWidgets();

	default ArrayList<WAbstractWidget> getAllWidgets() {
		ArrayList<WAbstractWidget> allWidgets = new ArrayList<>(getWidgets());
		for (WAbstractWidget widget : getWidgets()) {
			if (widget instanceof WCollection) {
				allWidgets.addAll(((WCollection) widget).getAllWidgets());
			}
		}
		return allWidgets;
	}
}
