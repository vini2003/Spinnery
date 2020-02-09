package spinnery.widget;

import spinnery.Spinnery;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WModifiableCollection;

public class WWidgetFactory {
    protected WModifiableCollection parent;

    public WWidgetFactory(WModifiableCollection parent) {
        this.parent = parent;
    }

    public <W extends WAbstractWidget> W build(Class<W> tClass, Position position, Size size) {
        W widget = WWidgetFactory.buildDetached(tClass, position, size);
        if (widget == null) return null;
        if (parent instanceof WAbstractWidget) {
            widget.setInterface(((WAbstractWidget) parent).getInterface());
        } else if (parent instanceof WInterface) {
            widget.setInterface((WInterface) parent);
        }
        return widget;
    }

    public static <W extends WAbstractWidget> W buildDetached(Class<W> tClass, Position position, Size size) {
        try {
            W widget = tClass.newInstance();
            if (position != null) widget.setPosition(position);
            if (size != null) widget.setSize(size);
            return widget;
        } catch (IllegalAccessException | InstantiationException e) {
            Spinnery.LOGGER.error("Could not create {}", tClass.getSimpleName(), e);
            return null;
        }
    }
}