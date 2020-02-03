package spinnery.widget.api;

import spinnery.util.MutablePair;
import spinnery.widget.WWidget;

import java.util.HashMap;
import java.util.Map;

public class WSize {
	protected Map<Integer, MutablePair<Integer, Integer>> sizes = new HashMap<>();

	public WSize put(int x, int y) {
		sizes.put(sizes.size(), MutablePair.of(x, y));
		return this;
	}

	public WSize put(WWidget widget) {
		sizes = widget.getSize().sizes;
		return this;
	}

	public void setX(int number, int sizeX) {
		this.sizes.get(number).setFirst(sizeX);
	}

	public void setY(int number, int sizeY) {
		this.sizes.get(number).setSecond(sizeY);
	}

	public int getX() {
		return sizes.get(0).getFirst();
	}

	public void setX(int sizeX) {
		this.sizes.get(0).setFirst(sizeX);
	}

	public int getY() {
		return sizes.get(0).getSecond();
	}

	public void setY(int sizeY) {
		this.sizes.get(0).setSecond(sizeY);
	}

	public int getX(int number) {
		return sizes.get(number).getFirst();
	}

	public int getY(int number) {
		return sizes.get(number).getSecond();
	}
}
