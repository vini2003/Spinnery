package spinnery.widget;

public abstract class WAbstractToggle extends WWidget {
	protected boolean toggleState = false;

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		setToggleState(!getToggleState());
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public <W extends WAbstractToggle> W setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
		return (W) this;
	}
}
