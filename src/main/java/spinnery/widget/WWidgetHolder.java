package spinnery.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WWidgetHolder {
	List<WWidget> heldWidgets = new ArrayList<>();

	public List<WWidget> getWidgets() {
		return heldWidgets;
	}

	public void add(WWidget... widgets) {
		heldWidgets.addAll(Arrays.asList(widgets));
	}

	public void remove(WWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
	}
}
