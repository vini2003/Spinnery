package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public interface WCollection {
	Set<WAbstractWidget> getWidgets();

	default Set<WAbstractWidget> getAllWidgets() {
		Set<WAbstractWidget> allWidgets = new HashSet<>(getWidgets());
		for (WAbstractWidget widget : getWidgets()) {
			if (widget instanceof WCollection) {
				allWidgets.addAll(((WCollection) widget).getAllWidgets());
			}
		}
		return allWidgets;
	}
}
