package spinnery.widget;

import spinnery.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class WSize {
	protected Map<Integer, Pair> sizes = new HashMap<>();

	public static WSize of(int x, int y) {
		WSize size = new WSize();
		size.add(Pair.of(x, y));
		return size;
	}

	public static WSize of(int x1, int y1, int x2, int y2) {
		WSize size = new WSize();
		size.add(Pair.of(x1, y1), Pair.of(x2, y2));
		return size;
	}

	public static WSize of(WWidget widget) {
		return WSize.of(widget.getWidth(), widget.getHeight());
	}

	public void add(Pair... pairs) {
		for (Pair pair : pairs) {
			sizes.put(sizes.size(), pair);
		}
	}

	public void setX(int sizeX) {
		this.sizes.get(0).setFirst(sizeX);
	}

	public void setY(int sizeY) {
		this.sizes.get(0).setSecond(sizeY);
	}

	public void setX(int number, int sizeX) {
		this.sizes.get(number).setFirst(sizeX);
	}

	public void setY(int number, int sizeY) {
		this.sizes.get(number).setSecond(sizeY);
	}

	public int getX() {
		return (int) sizes.get(0).getFirst();
	}

	public int getY() {
		return (int) sizes.get(0).getSecond();
	}

	public int getX(int number) {
		return (int) sizes.get(number).getFirst();
	}

	public int getY(int number) {
		return (int) sizes.get(number).getSecond();
	}
}
