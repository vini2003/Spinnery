package spinnery.widget.api;

/**
 * Generic interface representing a "viewport", i.e. an object that has a larger underlying area, of which only
 * a part is visible.
 */
public interface WViewport {
    Size getInnerSize();
    default int getInnerHeight() {
        return getInnerSize().getHeight();
    }
    default int getInnerWidth() {
        return getInnerSize().getWidth();
    }
    Size getVisibleSize();
    default int getVisibleHeight() {
        return getVisibleSize().getHeight();
    }
    default int getVisibleWidth() {
        return getVisibleSize().getWidth();
    }
}
