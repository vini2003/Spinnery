package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WWidgetFactory;

import java.util.function.Supplier;

/**
 * Generic interface representing a collection of widgets that may be modified by adding or removing widgets.
 * Modifiable collections provide a factory for creating child widgets, and expose convenience methods for
 * creating children.
 */
public interface WModifiableCollection extends WCollection {
	/**
	 * Adds the specified widgets to this collection. By convention, widgets added with this method are added as
	 * direct children, and as such should be contained in the Set returned by {@link #getWidgets()}.
	 *
	 * @param widgets widgets to add
	 */
	void add(WAbstractWidget... widgets);

	/**
	 * Removes the specified widgets from this collection. By convention, if passed widgets that are not direct
	 * children, this should be a no-op.
	 *
	 * @param widgets widgets to remove
	 */
	void remove(WAbstractWidget... widgets);

	/**
	 * Convenience method for short-circuiting <tt>getFactory().build(Class)</tt>.
	 *
	 * @param wClass widget class
	 * @return created widget
	 * @deprecated Reflective APIs for adding widgets are deprecated. Use {@link #createChild(Supplier)} instead.
	 */
	@Deprecated
	default <W extends WAbstractWidget> W createChild(Class<W> wClass) {
		return createChild(wClass, null, null);
	}

	/**
	 * Convenience method for short-circuiting <tt>factory.get()</tt>.
	 *
	 * @param factory widget factory
	 * @return created widget
	 */
	default <W extends WAbstractWidget> W createChild(Supplier<W> factory) {
		return createChild(factory, null, null);
	}

	/**
	 * Convenience method for short-circuiting <tt>getFactory().build(Class)</tt> and setting the widget's
	 * position and size.
	 *
	 * @param wClass   widget class
	 * @param position initial widget position
	 * @param size     initial widget size
	 * @return created widget
	 * @deprecated Reflective APIs for adding widgets are deprecated. Use {@link #createChild(Supplier, Position, Size)} instead.
	 */
	@Deprecated
	default <W extends WAbstractWidget> W createChild(Class<W> wClass, Position position, Size size) {
		return createChild(() -> getFactory().build(wClass), position, size);
	}

	/**
	 * Convenience method for short-circuiting <tt>factory.get()</tt> and setting the widget's
	 * position and size.
	 *
	 * @param factory  widget factory
	 * @param position initial widget position
	 * @param size     initial widget size
	 * @return created widget
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

	/**
	 * Gets the widget factory for creating children of this modifiable collection.
	 *
	 * @return widget factory instance
	 * @deprecated Reflective APIs for adding widgets are deprecated.
	 */
	@Deprecated
	default WWidgetFactory getFactory() {
		return new WWidgetFactory(this);
	}

	/**
	 * Convenience method for short-circuiting <tt>getFactory().build(Class)</tt> and setting the widget's
	 * position.
	 *
	 * @param wClass   widget class
	 * @param position initial widget position
	 * @return created widget
	 * @deprecated Reflective APIs for adding widgets are deprecated. Use {@link #createChild(Supplier, Position)} instead.
	 */
	@Deprecated
	default <W extends WAbstractWidget> W createChild(Class<W> wClass, Position position) {
		return createChild(wClass, position, null);
	}

	/**
	 * Convenience method for short-circuiting {@code factory.get()} and setting the widget's
	 * position.
	 *
	 * @param factory  widget factory
	 * @param position initial widget position
	 * @return created widget
	 */
	default <W extends WAbstractWidget> W createChild(Supplier<W> factory, Position position) {
		return createChild(factory, position, null);
	}
}
