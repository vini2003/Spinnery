package spinnery.widget.api;

public class WPosition implements WPositioned {
	public static final WPosition ORIGIN = new WPosition();

	protected WPositioned anchor = ORIGIN;
	protected int x;
	protected int y;
	protected int z;
	protected int offsetX;
	protected int offsetY;
	protected int offsetZ;

	protected WPosition() {
	}

	public static WPosition of(WPositioned source) {
		return new WPosition().set(source.getX(), source.getY(), source.getZ());
	}

	public static WPosition of(int x, int y, int z) {
		return new WPosition().set(x, y, z);
	}

	public static WPosition of(WPositioned anchor, int x, int y, int z) {
		return new WPosition().anchor(anchor).set(x, y, z);
	}

	public static WPosition of(WPositioned anchor, int x, int y) {
		return new WPosition().anchor(anchor).set(x, y, 0);
	}

	public static WPosition ofTopRight(WLayoutElement source) {
		return WPosition.of(source).add(source.getWidth(), 0, 0);
	}

	public static WPosition ofBottomLeft(WLayoutElement source) {
		return WPosition.of(source).add(0, source.getHeight(), 0);
	}

	public static WPosition ofBottomRight(WLayoutElement source) {
		return WPosition.of(source).add(source.getWidth(), source.getHeight(), 0);
	}

	public WPosition anchor(WPositioned anchor) {
		this.anchor = anchor;
		return this;
	}

	public WPosition set(int x, int y, int z) {
		setOffsetX(x);
		setOffsetY(y);
		setOffsetZ(z);

		setX(anchor.getX() + x);
		setY(anchor.getY() + y);
		setZ(anchor.getZ() + z);

		return this;
	}

	public WPosition add(int x, int y, int z) {
		WPosition newPos = WPosition.of(this);
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

	public WPosition setX(int x) {
		this.x = x;
		return this;
	}

	public WPosition setY(int y) {
		this.y = y;
		return this;
	}

	public WPosition setZ(int z) {
		this.z = z;
		return this;
	}

	public WPosition setOffsetX(int offsetX) {
		this.offsetX = offsetX;
		return this;
	}

	public WPosition setOffsetY(int offsetY) {
		this.offsetY = offsetY;
		return this;
	}

	public WPosition setOffsetZ(int offsetZ) {
		this.offsetZ = offsetZ;
		return this;
	}
}
