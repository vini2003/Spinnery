package spinnery.util;

public class Pair {
	private Object first, second;

	public static Pair of(Object first, Object second) {
		Pair pair = new Pair();
		pair.first = first;
		pair.second = second;
		return pair;
	}

	public Object getFirst() {
		return first;
	}

	public void setFirst(Object first) {
		this.first = first;
	}

	public Object getSecond() {
		return second;
	}

	public void setSecond(Object second) {
		this.second = second;
	}
}
