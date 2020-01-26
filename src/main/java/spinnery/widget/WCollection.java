package spinnery.widget;

import java.util.ArrayList;
import java.util.List;

public interface WCollection {
	List<WWidget> getWidgets();

	default List<WWidget> getAllWidgets() {
		List<WWidget> allWidgets = new ArrayList<>(getWidgets());
		for (WWidget widget : getWidgets()) {
			if (widget instanceof WCollection) {
				allWidgets.addAll(((WCollection) widget).getAllWidgets());
			}
		}
		return allWidgets;
	}

	boolean contains(WWidget... widgets);
}
