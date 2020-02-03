package spinnery.widget.api;

public interface WLayoutElement extends WPositioned, WSized {
    void draw();
    default void onLayoutChange() {}
}
