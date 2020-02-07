package spinnery.widget.api;

import java.util.Objects;

/**
 * Contains four directional dimensions, meant to be used like CSS padding (4-valued representations
 * are ordered clockwise from the top).
 */
public class WPadding {
    protected final int top;
    protected final int bottom;
    protected final int left;
    protected final int right;

    public static WPadding of(int top, int right, int bottom, int left) {
        return new WPadding(top, right, bottom, left);
    }

    public static WPadding of(int vertical, int horizontal) {
        return new WPadding(vertical, horizontal, vertical, horizontal);
    }

    public static WPadding of(int all) {
        return new WPadding(all, all, all, all);
    }

    public WPadding(int top, int right, int bottom, int left) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WPadding wPadding = (WPadding) o;
        return top == wPadding.top &&
                bottom == wPadding.bottom &&
                left == wPadding.left &&
                right == wPadding.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(top, bottom, left, right);
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
}
