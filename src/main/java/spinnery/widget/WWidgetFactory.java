package spinnery.widget;

import spinnery.Spinnery;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

public class WWidgetFactory {
    protected WModifiableCollection parent;

    public WWidgetFactory(WModifiableCollection parent) {
        this.parent = parent;
    }

    public <T extends WWidget> T build(Class<T> tClass, WPosition position, WSize size) {
        try {
            T widget = tClass.newInstance();

            if (position != null) widget.setPosition(position);
            if (size != null) widget.setSize(size);
            if (parent instanceof WWidget) {
                widget.setInterface(((WWidget) parent).getInterface());
            } else if (parent instanceof WInterface) {
                widget.setInterface((WInterface) parent);
            }

            return widget;
        } catch (IllegalAccessException | InstantiationException e) {
            Spinnery.LOGGER.error("Could not build {}", tClass.getSimpleName(), e);
            return null;
        }
    }
}