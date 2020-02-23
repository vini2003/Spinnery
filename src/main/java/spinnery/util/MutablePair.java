package spinnery.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class MutablePair<L, R> {
	private L first;
	private R second;

	public static <A, B> MutablePair<A, B> of(A first, B second) {
		MutablePair<A, B> pair = new MutablePair<>();
		pair.first = first;
		pair.second = second;
		return pair;
	}

	public L getFirst() {
		return first;
	}

	public MutablePair setFirst(L first) {
		this.first = first;
		return this;
	}

	public R getSecond() {
		return second;
	}

	public MutablePair setSecond(R second) {
		this.second = second;
		return this;
	}

	public MutablePair apply(Consumer<L> l, Consumer<R> r) {
		l.accept(first);
		r.accept(second);
		return this;
	}

	public MutablePair andThen(Consumer<MutablePair> c) {
		c.accept(this);
		return this;
	}
}
