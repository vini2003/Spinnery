package spinnery.widget.api;

/**
 * Generic interface representing a layout element that has an inner area. Generally, it is recommended for
 * widgets that have an inner area to implement {@link WPadded}, which provides a less generic and more standard
 * method of describing such widgets using a {@link Padding} property.
 */
public interface WInnerSized extends WLayoutElement {
    /**
     * Gets the size of the inner area.
     * @return size of inner area
     */
    Size getInnerSize();

    /**
     * Gets the position of the inner area's top left corner.
     * @return position of inner area
     */
    Position getInnerAnchor();
}
