package spinnery.widget.api;

public class WSize implements WSized {
	protected int width;
	protected int height;

	protected WSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public static WSize of(int width, int height) {
		return new WSize(width, height);
	}

	public static WSize of(WSized widget) {
		return new WSize(widget.getWidth(), widget.getHeight());
	}

	public int getWidth() {
		return width;
	}

	public WSize setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public WSize setHeight(int height) {
		this.height = height;
		return this;
	}
}
