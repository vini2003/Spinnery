package spinnery.widget.api;

import spinnery.widget.WWidget;

import java.util.LinkedHashSet;
import java.util.Set;

public interface WCollection {
	Set<WWidget> getWidgets();

	default Set<WWidget> getAllWidgets() {
		Set<WWidget> allWidgets = new LinkedHashSet<>(getWidgets());
		for (WWidget widget : getWidgets()) {
			if (widget instanceof WCollection) {
				allWidgets.addAll(((WCollection) widget).getAllWidgets());
			}
		}
		return allWidgets;
	}

	boolean contains(WWidget... widgets);
}
