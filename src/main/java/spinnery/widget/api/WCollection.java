package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

import java.util.LinkedHashSet;
import java.util.Set;


  add or remove widgets from the collection, and simply provides accessors to the collection's children.
 *
  @see WModifiableCollection
 /
public interface WCollection {


	  @author EngiN33R

	Set<WAbstractWidget> getWidgets();


	  The default implementation of this method does not check for cyclic references, so having the root

	  @author EngiN33R

	default Set<WAbstractWidget> getAllWidgets() {
		Set<WAbstractWidget> allWidgets = new LinkedHashSet<>(getWidgets());
		for (WAbstractWidget widget : getWidgets()) {
			if (widget instanceof WCollection) {
				allWidgets.addAll(((WCollection) widget).getAllWidgets());
			}
		}
		return allWidgets;
	}



	  @return whether widgets are contained
	 */
	boolean contains(WAbstractWidget... widgets);
}
