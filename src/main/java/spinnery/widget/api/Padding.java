package spinnery.widget.api;

import blue.endless.jankson.JsonElement;
import spinnery.util.JanksonUtilities;

import java.util.Objects;

/**
 * Contains four directional dimensions, meant to be used like CSS padding (4-valued representations
 * are ordered clockwise from the top).
 */
public class Padding implements JanksonSerializable {
	protected final int top;
	protected final int bottom;
	protected final int left;
	protected final int right;

	public Padding(int top, int right, int bottom, int left) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}

	public static Padding of(int top, int right, int bottom, int left) {
		return new Padding(top, right, bottom, left);
	}

	public static Padding of(int vertical, int horizontal) {
		return new Padding(vertical, horizontal, vertical, horizontal);
	}

	public static Padding of(int all) {
		return new Padding(all, all, all, all);
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return Objects.hash(top, bottom, left, right);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Padding padding = (Padding) o;
		return top == padding.top &&
				bottom == padding.bottom &&
				left == padding.left &&
				right == padding.right;
	}

	@Override
	public String toString() {
		return "WPadding{" +
				"top=" + top +
				", right=" + right +
				", bottom=" + bottom +
				", left=" + left +
				'}';
	}

	@Override
	public JsonElement toJson() {
		return JanksonUtilities.arrayOfPrimitives(top, right, bottom, left);
	}
}
