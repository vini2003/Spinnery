package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;

import java.util.function.Supplier;


  Modifiable collections provide a factory for creating child widgets, and expose convenience methods for
 /
public interface WModifiableCollection extends WCollection {

	  direct children, and as such should be contained in the Set returned by {@link #getWidgets()}.
	 *

	void add(WAbstractWidget... widgets);


	  children, this should be a no-op.
	 *

	void remove(WAbstractWidget... widgets);



	  @return created widget
	 */
	default <W extends WAbstractWidget> W createChild(Supplier<W> factory) {
		return createChild(factory, null, null);
	}


	  position and size.
	 *
	  @param position initial widget position
	  @return created widget
	 */
	default <W extends WAbstractWidget> W createChild(Supplier<? extends W> factory, Position position, Size size) {
		W widget = factory.get();
		if (position != null) widget.setPosition(position);
		if (size != null) widget.setSize(size);

		if (this instanceof WAbstractWidget) {
			widget.setInterface(((WAbstractWidget) this).getInterface());
		} else if (this instanceof WInterface) {
			widget.setInterface((WInterface) this);
		}

		if (this instanceof WLayoutElement) {
			widget.setParent((WLayoutElement) this);
		}

		add(widget);

		return widget;
	}


	  position.
	 *
	  @param position initial widget position

	default <W extends WAbstractWidget> W createChild(Supplier<W> factory, Position position) {
		return createChild(factory, position, null);
	}
}
