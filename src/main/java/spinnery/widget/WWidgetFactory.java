package spinnery.widget;

import com.google.common.base.Preconditions;
import spinnery.Spinnery;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;

/**
 * This class represents the mechanism for creating widgets given a widget type. Instances of this class must
 * have a non-null {@link WModifiableCollection} parent, which widgets created via {@link #build(Class)}
 * are automatically added to.
 *
 * @deprecated Reflective APIs for adding widgets are deprecated.
 */
@Deprecated
public class WWidgetFactory {
	protected WModifiableCollection parent;

	public WWidgetFactory(WModifiableCollection parent) {
		Preconditions.checkNotNull(parent, "widget factory must have valid parent");
		this.parent = parent;
	}

	/**
	 * Builds a widget given the class. Formally, this creates a widget using {@link #buildDetached(Class)} and
	 * assigns certain properties to the widget to make it a fully valid child of the factory's parent.
	 * These assignments include setting the widget's interface to the factory parent's (or the factory parent
	 * itself, if it is a WInterface), setting the widget's parent to the factory's parent, and adding the widget
	 * to the parent {@link WModifiableCollection}.
	 *
	 * @param tClass widget class
	 * @return created widget
	 * @see #buildDetached(Class)
	 */
	public <W extends WAbstractWidget> W build(Class<W> tClass) {
		W widget = WWidgetFactory.buildDetached(tClass);
		if (widget == null) return null;
		if (parent instanceof WAbstractWidget) {
			widget.setInterface(((WAbstractWidget) parent).getInterface());
		} else if (parent instanceof WInterface) {
			widget.setInterface((WInterface) parent);
		}
		if (parent instanceof WLayoutElement) widget.setParent((WLayoutElement) parent);
		parent.add(widget);
		return widget;
	}

	/**
	 * Builds a detached widget given the class. This is a type-safe operation that instantiates the
	 * widget using the default no-args constructor, returning null and logging an error if
	 * the instantiation call failed.
	 *
	 * @param tClass widget class
	 * @return constructed widget
	 */
	public static <W extends WAbstractWidget> W buildDetached(Class<W> tClass) {
		try {
			return tClass.newInstance();
		} catch (IllegalAccessException | InstantiationException e) {
			Spinnery.LOGGER.error("Could not create {}", tClass.getSimpleName(), e);
			return null;
		}
	}
}