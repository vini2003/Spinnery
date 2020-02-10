package spinnery.widget.api;

import java.util.List;

public interface WDrawableCollection extends WCollection {
	void recalculateCache();

	List<WLayoutElement> getOrderedWidgets();
}
