package spinnery.widget.api;

import blue.endless.jankson.JsonElement;
import spinnery.common.utilities.Janksons;

import java.util.Objects;
import java.util.function.Supplier;


 /
public class Size implements WSized, JanksonSerializable {
	protected float width;
	protected float height;

	protected float widthSupplied;
	protected float heightSupplied;

	protected Supplier<Float> widthSupplier;
	protected Supplier<Float> heightSupplier;

	protected Size(float width, float height) {
		this.width = width;
		this.height = height;
	}

	protected Size(Supplier<Float> widthSupplier, Supplier<Float> heightSupplier) {
		this.widthSupplier = widthSupplier;
		this.heightSupplier = heightSupplier;
	}

	public static Size of(float width, float height) {
		return new Size(width, height);
	}

	public static Size of(float side) {
		return new Size(side, side);
	}

	public static Size of(Supplier<Float> widthSupplier, Supplier<Float> heightSupplier) {
		Size size = new Size(widthSupplier, heightSupplier);
		size.onLayoutChange();
		return size;
	}

	public static Size of(Supplier<Float> sideSupplier) {
		return new Size(sideSupplier, sideSupplier);
	}




	public Size add(float width, float height) {
		Size newSize = Size.of(this);
		return newSize.setWidth(newSize.getWidth() + width).setHeight(newSize.getHeight() + height);
	}




	public static Size of(WSized widget) {
		return new Size(widget.getWidth(), widget.getHeight());
	}

	public float getWidth() {
		return widthSupplier != null ? widthSupplied : width;
	}

	public Size setWidth(float width) {
		this.width = width;
		return this;
	}

	public float getHeight() {
		return heightSupplier != null ? heightSupplied : height;
	}

	public Size setHeight(float height) {
		this.height = height;
		return this;
	}

	public boolean isLargerInWidthAndHeight(Size size) {
		return this.width > size.width && this.height > size.height;
	}

	public boolean isLargerInWidthOrHeight(Size size) {
		return this.width > size.width || this.height > size.height;
	}

	public boolean isLargerInWidth(Size size) {
		return this.width > size.width;
	}

	public boolean isLargerInHeight(Size size) {
		return this.height > size.height;
	}

	public boolean isLargerInArea(Size size) {
		return this.width * this.height > size.width * size.height;
	}

	public boolean isSmallerInWidthAndHeight(Size size) {
		return this.width < size.width && this.height < size.height;
	}

	public boolean isSmallerInWidthOrHeight(Size size) {
		return this.width < size.width || this.height < size.height;
	}

	public boolean isSmallerInWidth(Size size) {
		return this.width < size.width;
	}

	public boolean isSmallerInHeight(Size size) {
		return this.height < size.height;
	}

	public boolean isSmallerInArea(Size size) {
		return this.width * this.height < size.width * size.height;
	}

	public void onLayoutChange() {
		if (this.widthSupplier != null) widthSupplied = widthSupplier.get();
		if (this.heightSupplier != null) heightSupplied = heightSupplier.get();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Size size = (Size) o;
		return getWidth() == size.getWidth() &&
				getHeight() == size.getHeight();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getWidth(), getHeight());
	}

	@Override
	public JsonElement toJson() {
		return Janksons.arrayOfPrimitives(width, height);
	}
}
