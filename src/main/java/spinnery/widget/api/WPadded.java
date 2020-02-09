package spinnery.widget.api;

public interface WPadded extends WInnerSized {
    Padding getPadding();

    @Override
    default Size getInnerSize() {
        return Size.of(this).add(
                -(getPadding().getLeft() + getPadding().getRight()),
                -(getPadding().getTop() + getPadding().getBottom())
        );
    }

    @Override
    default Position getInnerAnchor() {
        return Position.of(this, getPadding().getLeft(), getPadding().getTop());
    }
}
