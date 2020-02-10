package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface WDrawableCollection extends WCollection {
	void recalculateCache();

	List<WLayoutElement> getOrderedWidgets();
}
