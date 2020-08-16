package spinnery.widget.api;

import blue.endless.jankson.JsonElement;
import spinnery.common.utilities.Janksons;

import java.util.Objects;
import java.util.function.Supplier;


  a position with all 0 coordinates.
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



	  @param y absolute y

	public static Position of(float x, float y, float z) {
		return new Position(ORIGIN).set(x, y, z);
	}



	  @param ySupplier supplier of y

	public static Position of(Supplier<Float> xSupplier, Supplier<Float> ySupplier, Supplier<Float> zSupplier) {
		Position position = new Position(ORIGIN).set(xSupplier, ySupplier, zSupplier);
		position.onLayoutChange();
		return position;
	}



	  @param y relative y

	public Position set(float x, float y, float z) {
		setRelativeX(x);
		setRelativeY(y);
		setRelativeZ(z);
		return this;
	}



	  @param ySupplier supplier of y

	public Position set(Supplier<Float> xSupplier, Supplier<Float> ySupplier, Supplier<Float> zSupplier) {
		setXSupplier(xSupplier);
		setYSupplier(ySupplier);
		setZSupplier(zSupplier);
		return this;
	}



	  @param x      relative x
	  @param z      relative z
	 */
	public static Position of(WPositioned anchor, float x, float y, float z) {
		return new Position(anchor).set(x, y, z);
	}



	  @param x      relative x

	public static Position of(WPositioned anchor, float x, float y) {
		return new Position(anchor).set(x, y, 0);
	}




	public static Position ofTopRight(WLayoutElement source) {
		return Position.of(source).add(source.getWidth(), 0, 0);
	}



	  @param y increment relative y
	  @return new position object
	 */
	public Position add(float x, float y, float z) {
		Position newPos = Position.of(this);
		newPos.set(newPos.x + x, newPos.y + y, newPos.z + z);
		return newPos;
	}



	  @return equivalent position
	 */
	public static Position of(WPositioned source) {
		return new Position(source);
	}




	public static Position ofBottomLeft(WLayoutElement source) {
		return Position.of(source).add(0, source.getHeight(), 0);
	}




	public static Position ofBottomRight(WLayoutElement source) {
		return Position.of(source).add(source.getWidth(), source.getHeight(), 0);
	}



	  @param y offset y
	  @return same position object
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


	  position's relative coordiate, and this position's offset coordiante.
	 *

	public float getX() {
		return anchor == null ? 0 : xSupplier == null ? anchor.getX() + x + offsetX : xSupplied;
	}


	  position's relative coordiate, and this position's offset coordiante.
	 *

	public float getY() {
		return anchor == null ? 0 : ySupplier == null ? anchor.getY() + y + offsetY : ySupplied;
	}


	  position's relative coordiate, and this position's offset coordiante.
	 *

	public float getZ() {
		return anchor == null ? 0 : zSupplier == null ? anchor.getZ() + z + offsetZ : zSupplied;
	}


	  equal to the parameter.
	 *
	  @return same position object
	 */
	public Position setZ(float z) {
		return setRelativeZ(z - anchor.getZ() - offsetZ);
	}


	  equal to the parameter.
	 *
	  @return same position object
	 */
	public Position setY(float y) {
		return setRelativeY(y - anchor.getY() - offsetY);
	}


	  equal to the parameter.
	 *
	  @return same position object
	 */
	public Position setX(float x) {
		return setRelativeX(x - anchor.getX() - offsetX);
	}



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
		return Janksons.arrayOfPrimitives(x, y, z);
	}
}
