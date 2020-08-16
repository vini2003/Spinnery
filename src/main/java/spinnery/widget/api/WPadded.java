package spinnery.widget.api;


  the inner anchor and size.
 */
public interface WPadded extends WInnerSized {
	@Override
	default Size getInnerSize() {
		return Size.of(this).add(
				-(getPadding().getLeft() + getPadding().getRight()),
				-(getPadding().getTop() + getPadding().getBottom())
		);
	}

	Padding getPadding();

	@Override
	default Position getInnerAnchor() {
		return Position.of(this, getPadding().getLeft(), getPadding().getTop());
	}
}
