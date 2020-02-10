package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;
import spinnery.widget.WWidgetFactory;

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
	 */
	default <W extends WAbstractWidget> W createChild(Class<W> wClass) {
		return createChild(wClass, null, null);
	}

	/**
	 * Convenience method for short-circuiting <tt>getFactory().build(Class)</tt> and setting the widget's
	 * position and size.
	 *
	 * @param wClass   widget class
	 * @param position initial widget position
	 * @param size     initial widget size
	 * @return created widget
	 */
	default <W extends WAbstractWidget> W createChild(Class<W> wClass, Position position, Size size) {
		W widget = getFactory().build(wClass);
		if (position != null) widget.setPosition(position);
		if (size != null) widget.setSize(size);
		return widget;
	}

	/**
	 * Gets the widget factory for creating children of this modifiable collection.
	 *
	 * @return widget factory instance
	 */
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
	 */
	default <W extends WAbstractWidget> W createChild(Class<W> wClass, Position position) {
		return createChild(wClass, position, null);
	}
}
