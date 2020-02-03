package spinnery.widget.api;

import spinnery.widget.WWidget;

public class WPosition {
	protected WWidget anchor;
	protected int x;
	protected int y;
	protected int z;
	protected int rawX;
	protected int rawY;
	protected int rawZ;

	public WPosition anchor(WWidget anchor) {
		this.anchor = anchor;
		return this;
	}

	public WPosition position(int x, int y, int z) {
		setRawX(x);
		setRawY(y);
		setRawZ(z);

		if (anchor == null) return this;

		setX(anchor.getX() + getRawX());
		setY(anchor.getY() + getRawY());
		setZ(anchor.getZ() + getRawZ());

		return this;
	}

	public void align() {
		if (anchor == null) return;

		setX(anchor.getX() + getRawX());
		setY(anchor.getY() + getRawY());
		setZ(anchor.getZ() + getRawZ());
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getRawX() {
		return rawX;
	}

	public void setRawX(int rawX) {
		this.rawX = rawX;
	}

	public int getRawY() {
		return rawY;
	}

	public void setRawY(int rawY) {
		this.rawY = rawY;
	}

	public int getRawZ() {
		return rawZ;
	}

	public void setRawZ(int rawZ) {
		this.rawZ = rawZ;
	}

	public WWidget getAnchor() {
		return anchor;
	}
}
