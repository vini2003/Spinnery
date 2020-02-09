package spinnery.widget;

import com.google.common.base.Preconditions;
import spinnery.Spinnery;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;

public class WWidgetFactory {
    protected WModifiableCollection parent;

    public WWidgetFactory(WModifiableCollection parent) {
        Preconditions.checkNotNull(parent, "widget factory must have valid parent");
        this.parent = parent;
    }

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

    public static <W extends WAbstractWidget> W buildDetached(Class<W> tClass) {
        try {
            return tClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            Spinnery.LOGGER.error("Could not create {}", tClass.getSimpleName(), e);
            return null;
        }
    }
}