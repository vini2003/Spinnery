package spinnery.widget.declaration.size;

public interface WSized {
	default float getWidth() {
		return 0;
	}

	default float getHeight() {
		return 0;
	}
}
