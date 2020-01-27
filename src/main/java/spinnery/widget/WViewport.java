package spinnery.widget;

public interface WViewport {
    WSize getInnerSize();
    default int getInnerHeight() {
        return getInnerSize().getY();
    }
    default int getInnerWidth() {
        return getInnerSize().getX();
    }
    WSize getVisibleSize();
    default int getVisibleHeight() {
        return getVisibleSize().getY();
    }
    default int getVisibleWidth() {
        return getVisibleSize().getX();
    }
}
