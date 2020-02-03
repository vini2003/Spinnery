package spinnery.widget.api;

public interface WViewport {
    WSize getInnerSize();
    default int getInnerHeight() {
        return getInnerSize().getHeight();
    }
    default int getInnerWidth() {
        return getInnerSize().getWidth();
    }
    WSize getVisibleSize();
    default int getVisibleHeight() {
        return getVisibleSize().getHeight();
    }
    default int getVisibleWidth() {
        return getVisibleSize().getWidth();
    }
}
