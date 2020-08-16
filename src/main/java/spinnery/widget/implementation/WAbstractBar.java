package spinnery.widget.implementation;

import java.util.function.Supplier;

public abstract class WAbstractBar extends WAbstractWidget {
	private Supplier<Number> limitSupplier;
	private Supplier<Number> progressSupplier;

	public Supplier<Number> getLimitSupplier() {
		return limitSupplier;
	}

	public <W extends WAbstractBar> W setLimitSupplier(Supplier<Number> limitSupplier) {
		this.limitSupplier = limitSupplier;
		return (W) this;
	}

	public Supplier<Number> getProgressSupplier() {
		return progressSupplier;
	}

	public <W extends WAbstractBar> W setProgressSupplier(Supplier<Number> progressSupplier) {
		this.progressSupplier = progressSupplier;
		return (W) this;
	}

	public int getLimit() {
		return limitSupplier.get().intValue();
	}

	public int getProgress() {
		return progressSupplier.get().intValue();
	}
}