package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
