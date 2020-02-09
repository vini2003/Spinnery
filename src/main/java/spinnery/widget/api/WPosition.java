package spinnery.widget.api;

public class WPosition implements WPositioned {
	public static final WPosition ORIGIN = new WPosition(new WPositioned() {
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

	protected WPosition(WPositioned anchor) {
		this.anchor = anchor;
	}

	public static WPosition origin() {
		return new WPosition(ORIGIN);
	}

	public static WPosition of(WPositioned source) {
		return new WPosition(source);
	}

	public static WPosition of(int x, int y, int z) {
		return new WPosition(ORIGIN).set(x, y, z);
	}

	public static WPosition of(WPositioned anchor, int x, int y, int z) {
		return new WPosition(anchor).set(x, y, z);
	}

	public static WPosition of(WPositioned anchor, int x, int y) {
		return new WPosition(anchor).set(x, y, 0);
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
		setRelativeX(x);
		setRelativeY(y);
		setRelativeZ(z);
		return this;
	}

	public WPosition setOffset(int x, int y, int z) {
		setOffsetX(x);
		setOffsetY(y);
		setOffsetZ(z);
		return this;
	}

	public WPosition add(int x, int y, int z) {
		WPosition newPos = WPosition.of(this);
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

	public WPosition setX(int x) {
		return setRelativeX(x - anchor.getX());
	}

	public WPosition setY(int y) {
		return setRelativeY(y - anchor.getY());
	}

	public WPosition setZ(int z) {
		return setRelativeZ(z - anchor.getZ());
	}

	public WPosition setRelativeX(int x) {
		this.x = x;
		return this;
	}

	public WPosition setRelativeY(int y) {
		this.y = y;
		return this;
	}

	public WPosition setRelativeZ(int offsetZ) {
		this.z = offsetZ;
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
