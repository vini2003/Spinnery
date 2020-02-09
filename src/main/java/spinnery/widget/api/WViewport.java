package spinnery.widget.api;

/**
 * Generic interface representing a "viewport", i.e. an object that has a larger underlying area, of which only
 * a part is visible.
 */
public interface WViewport {
    Size getUnderlyingSize();
    default int getUnderlyingHeight() {
        return getUnderlyingSize().getHeight();
    }
    default int getUnderlyingWidth() {
        return getUnderlyingSize().getWidth();
    }
    Size getVisibleSize();
    default int getVisibleHeight() {
        return getVisibleSize().getHeight();
    }
    default int getVisibleWidth() {
        return getVisibleSize().getWidth();
    }
}
