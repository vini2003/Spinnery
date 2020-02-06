package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

import java.util.LinkedHashSet;
import java.util.Set;

public interface WCollection {
	Set<WAbstractWidget> getWidgets();

	default Set<WAbstractWidget> getAllWidgets() {
		Set<WAbstractWidget> allWidgets = new LinkedHashSet<>(getWidgets());
		for (WAbstractWidget widget : getWidgets()) {
			if (widget instanceof WCollection) {
				allWidgets.addAll(((WCollection) widget).getAllWidgets());
			}
		}
		return allWidgets;
	}

	boolean contains(WAbstractWidget... widgets);
}
