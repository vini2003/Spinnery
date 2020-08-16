package spinnery.widget.api;

public interface WSized {
	default float getWidth() {
		return 0;
	}

	default float getHeight() {
		return 0;
	}
}
