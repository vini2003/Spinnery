package spinnery.widget;

public class WPosition {
	public enum WType {
		ANCHORED,
		FREE
	}

	protected WType type;
	protected WWidget anchor;
	protected int x;
	protected int y;
	protected int z;
	protected int rawX;
	protected int rawY;
	protected int rawZ;

	public static WPosition of(WType type, int x, int y, int z, WWidget... anchor) {
		WPosition position = new WPosition();
		position.type = type;
		if (anchor.length > 0) {
			position.anchor = anchor[0];
		}
		position.set(x, y, z);
		return position;
	}

	public void align() {
		switch (type) {
			case ANCHORED:
				setX(anchor.getX() + getRawX());
				setY(anchor.getY() + getRawY());
				setZ(anchor.getZ() + getRawZ());
				break;
			case FREE:
				set(getRawX(), getRawY(), getRawZ());
				break;
		}
	}

	public void set(int x, int y, int z) {
		switch (type) {
			case ANCHORED:
				setRaw(x, y, z);
				setX(anchor.getX() + x);
				setY(anchor.getY() + y);
				setZ(anchor.getZ() + z);
				break;
			case FREE:
				setRaw(x, y, z);
				setX(getRawX());
				setY(getRawY());
				setZ(getRawZ());
				break;
		}
	}

	public void setX(int x) {
		this.x = x + (type == WType.ANCHORED ? getRawX() : 0);
	}

	public void setY(int y) {
		this.y = y + (type == WType.ANCHORED ? getRawY() : 0);
	}

	public void setZ(int z) {
		this.z = z + (type == WType.ANCHORED ? getRawZ() : 0);
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

	public void setRaw(int x, int y, int z) {
		setRawX(x);
		setRawY(y);
		setRawZ(z);
	}

	public void setRawX(int rawX) {
		this.rawX = rawX;
	}

	public void setRawY(int rawY) {
		this.rawY = rawY;
	}

	public void setRawZ(int rawZ) {
		this.rawZ = rawZ;
	}

	public int getRawX() {
		return rawX;
	}

	public int getRawY() {
		return rawY;
	}

	public int getRawZ() {
		return rawZ;
	}
}
