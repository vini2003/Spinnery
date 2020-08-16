package spinnery.widget.declaration.position;

public interface WPositioned {
	default float getX() {
		return 0;
	}

	default float getY() {
		return 0;
	}
}
