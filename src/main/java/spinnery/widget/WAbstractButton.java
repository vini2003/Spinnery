package spinnery.widget;


  general button-like widget, like a {@link WButton}
 /
public abstract class WAbstractButton extends WAbstractWidget {
	protected boolean lowered = false;
	protected int ticks = 0;
	protected int delayTicks = 1;



	@Override
	public void tick() {
		lowered = ticks > 0;
		ticks -= ticks > 0 ? 1 : 0;
	}



	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		setLowered(true);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
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
