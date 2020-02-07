package spinnery.widget.api;

public interface WPadded extends WInnerSized {
    WPadding getPadding();

    @Override
    default WSize getInnerSize() {
        return WSize.of(this).add(
                -(getPadding().getLeft() + getPadding().getRight()),
                -(getPadding().getTop() + getPadding().getBottom())
        );
    }

    @Override
    default WPosition getInnerAnchor() {
        return WPosition.of(this, getPadding().getLeft(), getPadding().getTop());
    }
}
