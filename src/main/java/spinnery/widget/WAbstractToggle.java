package spinnery.widget;

public abstract class WAbstractToggle extends WAbstractWidget {
	protected boolean isToggled = false;

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		setToggled(!isToggled());
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean isToggled() {
		return isToggled;
	}

	public <W extends WAbstractToggle> W setToggled(boolean toggled) {
		this.isToggled = toggled;
		return (W) this;
	}
}
