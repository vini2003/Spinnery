package spinnery.widget.api;

import blue.endless.jankson.JsonElement;
import spinnery.common.utility.JanksonUtilities;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Data class representing a position offset relative to an anchor. By default, the anchor is ORIGIN,
 * a position with all 0 coordinates.
 */
public class Position implements WPositioned, JanksonSerializable {
	public static final Position ORIGIN = Position.of(0, 0, 0);

	protected WPositioned anchor;

	protected float x;
	protected float y;
	protected float z;

	protected float offsetX;
	protected float offsetY;
	protected float offsetZ;

	protected float xSupplied;
	protected float ySupplied;
	protected float zSupplied;

	protected Supplier<Float> xSupplier;
	protected Supplier<Float> ySupplier;
	protected Supplier<Float> zSupplier;

	protected Position(WPositioned anchor) {
		this.anchor = anchor;
	}

	public static Position origin() {
		return new Position(ORIGIN);
	}

	/**
	 * Creates a position with the given "absolute" (relative to ORIGIN) coordinates.
	 *
	 * @param x absolute x
	 * @param y absolute y
	 * @param z absolute z
	 */
	public static Position of(float x, float y, float z) {
		return new Position(ORIGIN).set(x, y, z);
	}

	/**
	 * Creates a position with the given supplier-based coordinates.
	 *
	 * @param xSupplier supplier of x
	 * @param ySupplier supplier of y
	 * @param zSupplier supplier of z
	 */
	public static Position of(Supplier<Float> xSupplier, Supplier<Float> ySupplier, Supplier<Float> zSupplier) {
		Position position = new Position(ORIGIN).set(xSupplier, ySupplier, zSupplier);
		position.onLayoutChange();
		return position;
	}

	/**
	 * Sets new coordinates of this position object relative to its anchor.
	 *
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 */
	public Position set(float x, float y, float z) {
		setRelativeX(x);
		setRelativeY(y);
		setRelativeZ(z);
		return this;
	}

	/**
	 * Sets new coordinate suppliers of this position object.
	 *
	 * @param xSupplier supplier of x
	 * @param ySupplier supplier of y
	 * @param zSupplier supplier of z
	 */
	public Position set(Supplier<Float> xSupplier, Supplier<Float> ySupplier, Supplier<Float> zSupplier) {
		setXSupplier(xSupplier);
		setYSupplier(ySupplier);
		setZSupplier(zSupplier);
		return this;
	}

	/**
	 * Creates a position with the given coordinates relative to anchor.
	 *
	 * @param anchor anchor
	 * @param x      relative x
	 * @param y      relative y
	 * @param z      relative z
	 */
	public static Position of(WPositioned anchor, float x, float y, float z) {
		return new Position(anchor).set(x, y, z);
	}

	/**
	 * Creates a position with the given coordinates relative to anchor and 0 Z offset.
	 *
	 * @param anchor anchor
	 * @param x      relative x
	 * @param y      relative y
	 */
	public static Position of(WPositioned anchor, float x, float y) {
		return new Position(anchor).set(x, y, 0);
	}

	/**
	 * Creates a position equivalent to the top-right corner of the given layout element (anchor + element width).
	 *
	 * @param source layout element
	 */
	public static Position ofTopRight(WLayoutElement source) {
		return Position.of(source).add(source.getWidth(), 0, 0);
	}

	/**
	 * Copies this position object and increments its coordinates by the given parameters.
	 *
	 * @param x increment relative x
	 * @param y increment relative y
	 * @param z increment relative z
	 * @return new position object
	 */
	public Position add(float x, float y, float z) {
		Position newPos = Position.of(this);
		newPos.set(newPos.x + x, newPos.y + y, newPos.z + z);
		return newPos;
	}

	/**
	 * Creates a copy of the given positioned element's position.
	 *
	 * @param source positioned element
	 * @return equivalent position
	 */
	public static Position of(WPositioned source) {
		return new Position(source);
	}

	/**
	 * Creates a position equivalent to the bottom-left corner of the given layout element (anchor + element height).
	 *
	 * @param source layout element
	 */
	public static Position ofBottomLeft(WLayoutElement source) {
		return Position.of(source).add(0, source.getHeight(), 0);
	}

	/**
	 * Creates a position equivalent to the bottom-right corner of the given layout element (anchor + element size).
	 *
	 * @param source layout element
	 */
	public static Position ofBottomRight(WLayoutElement source) {
		return Position.of(source).add(source.getWidth(), source.getHeight(), 0);
	}

	/**
	 * Sets new offset coordiantes of this position object.
	 *
	 * @param x offset x
	 * @param y offset y
	 * @param z offset z
	 * @return same position object
	 */
	public Position setOffset(float x, float y, float z) {
		setOffsetX(x);
		setOffsetY(y);
		setOffsetZ(z);
		return this;
	}

	public WPositioned getAnchor() {
		return anchor;
	}

	public Position setAnchor(WPositioned anchor) {
		this.anchor = anchor;
		return this;
	}

	/**
	 * Gets the absolute X coordinate, which is calculated as the sum of the anchor's coordinate, this
	 * position's relative coordiate, and this position's offset coordiante.
	 *
	 * @return absolute coordinate
	 */
	public float getX() {
		return anchor == null ? 0 : xSupplier == null ? anchor.getX() + x + offsetX : xSupplied;
	}

	/**
	 * Gets the absolute Y coordinate, which is calculated as the sum of the anchor's coordinate, this
	 * position's relative coordiate, and this position's offset coordiante.
	 *
	 * @return absolute coordinate
	 */
	public float getY() {
		return anchor == null ? 0 : ySupplier == null ? anchor.getY() + y + offsetY : ySupplied;
	}

	/**
	 * Gets the absolute Y coordinate, which is calculated as the sum of the anchor's coordinate, this
	 * position's relative coordiate, and this position's offset coordiante.
	 *
	 * @return absolute coordinate
	 */
	public float getZ() {
		return anchor == null ? 0 : zSupplier == null ? anchor.getZ() + z + offsetZ : zSupplied;
	}

	/**
	 * Changes this position objects's relative Z coordinate in such a way that its absolute X coordinate will be
	 * equal to the parameter.
	 *
	 * @param z absolute coordinate
	 * @return same position object
	 */
	public Position setZ(float z) {
		return setRelativeZ(z - anchor.getZ() - offsetZ);
	}

	/**
	 * Changes this position objects's relative Y coordinate in such a way that its absolute X coordinate will be
	 * equal to the parameter.
	 *
	 * @param y absolute coordinate
	 * @return same position object
	 */
	public Position setY(float y) {
		return setRelativeY(y - anchor.getY() - offsetY);
	}

	/**
	 * Changes this position objects's relative X coordinate in such a way that its absolute X coordinate will be
	 * equal to the parameter.
	 *
	 * @param x absolute coordinate
	 * @return same position object
	 */
	public Position setX(float x) {
		return setRelativeX(x - anchor.getX() - offsetX);
	}

	/**
	 * Updates this position's supplied coordinates.
	 */
	public Position onLayoutChange() {
		if (this.xSupplier != null) this.xSupplied = xSupplier.get();
		if (this.ySupplier != null) this.ySupplied = ySupplier.get();
		if (this.zSupplier != null) this.zSupplied = zSupplier.get();
		return this;
	}

	public float getRelativeX() {
		return x;
	}

	public Position setRelativeX(float x) {
		this.x = x;
		return this;
	}

	public float getRelativeY() {
		return y;
	}

	public Position setRelativeY(float y) {
		this.y = y;
		return this;
	}

	public float getRelativeZ() {
		return z;
	}

	public Position setRelativeZ(float offsetZ) {
		this.z = offsetZ;
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

	public float getOffsetZ() {
		return offsetZ;
	}

	public Position setOffsetZ(float offsetZ) {
		this.offsetZ = offsetZ;
		return this;
	}

	public Supplier<Float> getXSupplier() {
		return xSupplier;
	}

	public void setXSupplier(Supplier<Float> xSupplier) {
		this.xSupplier = xSupplier;
	}

	public Supplier<Float> getYSupplier() {
		return ySupplier;
	}

	public void setYSupplier(Supplier<Float> ySupplier) {
		this.ySupplier = ySupplier;
	}

	public Supplier<Float> getZSupplier() {
		return zSupplier;
	}

	public void setZSupplier(Supplier<Float> zSupplier) {
		this.zSupplier = zSupplier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return getX() == position.getX() &&
				getY() == position.getY() &&
				getZ() == position.getZ();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAnchor(), getX(), getY(), getZ());
	}

	@Override
	public JsonElement toJson() {
		return JanksonUtilities.arrayOfPrimitives(x, y, z);
	}
}
