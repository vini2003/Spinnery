package spinnery.widget.api;

public class Position implements WPositioned {
	public static final Position ORIGIN = new Position(new WPositioned() {
		@Override
		public int getX() {
			return 0;
		}

		@Override
		public int getY() {
			return 0;
		}

		@Override
		public int getZ() {
			return 0;
		}
	});

	protected WPositioned anchor;
	protected int x;
	protected int y;
	protected int z;
	protected int offsetX;
	protected int offsetY;
	protected int offsetZ;

	protected Position(WPositioned anchor) {
		this.anchor = anchor;
	}

	public static Position origin() {
		return new Position(ORIGIN);
	}

	public static Position of(WPositioned source) {
		return new Position(source);
	}

	public static Position of(int x, int y, int z) {
		return new Position(ORIGIN).set(x, y, z);
	}

	public static Position of(WPositioned anchor, int x, int y, int z) {
		return new Position(anchor).set(x, y, z);
	}

	public static Position of(WPositioned anchor, int x, int y) {
		return new Position(anchor).set(x, y, 0);
	}

	public static Position ofTopRight(WLayoutElement source) {
		return Position.of(source).add(source.getWidth(), 0, 0);
	}

	public static Position ofBottomLeft(WLayoutElement source) {
		return Position.of(source).add(0, source.getHeight(), 0);
	}

	public static Position ofBottomRight(WLayoutElement source) {
		return Position.of(source).add(source.getWidth(), source.getHeight(), 0);
	}

	public Position anchor(WPositioned anchor) {
		this.anchor = anchor;
		return this;
	}

	public Position set(int x, int y, int z) {
		setRelativeX(x);
		setRelativeY(y);
		setRelativeZ(z);
		return this;
	}

	public Position setOffset(int x, int y, int z) {
		setOffsetX(x);
		setOffsetY(y);
		setOffsetZ(z);
		return this;
	}

	public Position add(int x, int y, int z) {
		Position newPos = Position.of(this);
		newPos.set(newPos.x + x, newPos.y + y, newPos.z + z);
		return newPos;
	}

	public WPositioned getAnchor() {
		return anchor;
	}

	public int getX() {
		return anchor.getX() + x + offsetX;
	}

	public int getY() {
		return anchor.getY() + y + offsetY;
	}

	public int getZ() {
		return anchor.getZ() + z + offsetZ;
	}

	public int getRelativeX() {
		return x;
	}

	public int getRelativeY() {
		return y;
	}

	public int getRelativeZ() {
		return z;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public int getOffsetZ() {
		return offsetZ;
	}

	public Position setX(int x) {
		return setRelativeX(x - anchor.getX());
	}

	public Position setY(int y) {
		return setRelativeY(y - anchor.getY());
	}

	public Position setZ(int z) {
		return setRelativeZ(z - anchor.getZ());
	}

	public Position setRelativeX(int x) {
		this.x = x;
		return this;
	}

	public Position setRelativeY(int y) {
		this.y = y;
		return this;
	}

	public Position setRelativeZ(int offsetZ) {
		this.z = offsetZ;
		return this;
	}

	public Position setOffsetX(int offsetX) {
		this.offsetX = offsetX;
		return this;
	}

	public Position setOffsetY(int offsetY) {
		this.offsetY = offsetY;
		return this;
	}

	public Position setOffsetZ(int offsetZ) {
		this.offsetZ = offsetZ;
		return this;
	}
}
