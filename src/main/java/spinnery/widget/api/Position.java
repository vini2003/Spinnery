package spinnery.widget.api;

public class Position implements WPositioned {
	public static final Position ORIGIN = new Position();

	protected WPositioned anchor = ORIGIN;
	protected int x;
	protected int y;
	protected int z;
	protected int offsetX;
	protected int offsetY;
	protected int offsetZ;

	protected Position() {
	}

	public static Position of(WPositioned source) {
		return new Position().set(source.getX(), source.getY(), source.getZ());
	}

	public static Position of(int x, int y, int z) {
		return new Position().set(x, y, z);
	}

	public static Position of(WPositioned anchor, int x, int y, int z) {
		return new Position().anchor(anchor).set(x, y, z);
	}

	public static Position of(WPositioned anchor, int x, int y) {
		return new Position().anchor(anchor).set(x, y, 0);
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
		setOffsetX(x);
		setOffsetY(y);
		setOffsetZ(z);

		setX(anchor.getX() + x);
		setY(anchor.getY() + y);
		setZ(anchor.getZ() + z);

		return this;
	}

	public Position add(int x, int y, int z) {
		Position newPos = Position.of(this);
		newPos.set(newPos.x + x, newPos.y + y, newPos.z + z);
		return newPos;
	}

	public void align() {
		setX(anchor.getX() + getOffsetX());
		setY(anchor.getY() + getOffsetY());
		setZ(anchor.getZ() + getOffsetZ());
	}

	public WPositioned getAnchor() {
		return anchor;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
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
		this.x = x;
		return this;
	}

	public Position setY(int y) {
		this.y = y;
		return this;
	}

	public Position setZ(int z) {
		this.z = z;
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
