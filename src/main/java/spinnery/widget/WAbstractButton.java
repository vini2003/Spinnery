package spinnery.widget;

public abstract class WAbstractButton extends WAbstractWidget {
	protected boolean lowered = false;
	protected int ticks = 0;
	protected int delayTicks = 0;

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		setLowered(true);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void tick() {
		setLowered(ticks > 0);
		ticks -= ticks > 0 ? 1 : 0;
	}

	public int getDelay() {
		return delayTicks;
	}

	public <W extends WAbstractButton> W setDelay(int delayTicks) {
		this.delayTicks = delayTicks;
		return (W) this;
	}

	public boolean isLowered() {
		return lowered;
	}

	public <W extends WAbstractButton> W setLowered(boolean toggleState) {
		this.lowered = toggleState;
		this.ticks = toggleState ? getDelay() : 0;
		return (W) this;
	}
}
