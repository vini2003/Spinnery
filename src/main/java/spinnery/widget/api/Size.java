package spinnery.widget.api;

public class Size implements WSized {
	protected int width;
	protected int height;

	protected Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public static Size of(int width, int height) {
		return new Size(width, height);
	}

	public static Size of(WSized widget) {
		return new Size(widget.getWidth(), widget.getHeight());
	}

	public Size add(int width, int height) {
		Size newSize = Size.of(this);
		return newSize.setWidth(newSize.getWidth() + width).setHeight(newSize.getHeight() + height);
	}

	public int getWidth() {
		return width;
	}

	public Size setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public Size setHeight(int height) {
		this.height = height;
		return this;
	}
}
