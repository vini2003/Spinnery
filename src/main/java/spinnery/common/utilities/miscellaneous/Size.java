package spinnery.common.utilities.miscellaneous;

import spinnery.widget.declaration.size.WSized;

import java.util.Objects;

public class Size implements WSized {
	protected float width;
	protected float height;

	protected Size(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public static Size of(float width, float height) {
		return new Size(width, height);
	}

	public static Size of(float side) {
		return new Size(side, side);
	}

	public static Size of(WSized widget) {
		return new Size(widget.getWidth(), widget.getHeight());
	}

	public Size add(float width, float height) {
		Size newSize = Size.of(this);
		return newSize.setWidth(newSize.getWidth() + width).setHeight(newSize.getHeight() + height);
	}

	@Override
	public float getWidth() {
		return width;
	}

	public Size setWidth(float width) {
		this.width = width;
		return this;
	}

	@Override
	public float getHeight() {
		return height;
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
}
