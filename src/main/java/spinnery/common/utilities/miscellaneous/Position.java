package spinnery.common.utilities.miscellaneous;

import spinnery.widget.declaration.position.WPositioned;

import java.util.Objects;

public class Position implements WPositioned {
	public static final Position ORIGIN = Position.of(0, 0);

	protected WPositioned anchor;

	protected float x;
	protected float y;

	protected float offsetX;
	protected float offsetY;

	protected Position(WPositioned anchor) {
		this.anchor = anchor;
	}

	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public static Position origin() {
		return new Position(ORIGIN);
	}

	public static Position of(float x, float y) {
		return new Position(x, y);
	}

	public static Position of(WPositioned anchor) {
		return new Position(anchor);
	}

	public static Position of(WPositioned anchor, float x, float y) {
		return new Position(anchor).set(x, y);
	}

	public WPositioned getAnchor() {
		return anchor;
	}

	public Position setAnchor(WPositioned anchor) {
		this.anchor = anchor;
		return this;
	}

	public Position set(float x, float y) {
		setRawX(x);
		setRawY(y);
		return this;
	}

	public Position setOffset(float x, float y) {
		setOffsetX(x);
		setOffsetY(y);
		return this;
	}

	public Position add(float x, float y) {
		Position newPos = Position.of(this);
		newPos.set(newPos.x + x, newPos.y + y);
		return newPos;
	}

	public float getX() {
		return anchor == null ? x + offsetX : anchor.getX() + y + offsetY;
	}

	public float getY() {
		return anchor == null ? y + offsetY : anchor.getY() + y + offsetY;
	}

	public Position setY(float y) {
		return setRawY(y - anchor.getY() - offsetY);
	}

	public Position setX(float x) {
		return setRawX(x - anchor.getX() - offsetX);
	}

	public float getRawX() {
		return x;
	}

	public Position setRawX(float x) {
		this.x = x;
		return this;
	}

	public float getRawY() {
		return y;
	}

	public Position setRawY(float y) {
		this.y = y;
		return this;
	}

	public float getOffsetX() {
		return offsetX;
	}

	public Position setOffsetX(float offsetX) {
		this.offsetX = offsetX;
		return this;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public Position setOffsetY(float offsetY) {
		this.offsetY = offsetY;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return getX() == position.getX() && getY() == position.getY();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAnchor(), getX(), getY());
	}
}
