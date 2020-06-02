package spinnery.widget;

/**
 * A WAbstractBar provides the basics necessary for a
 * general toggle-like widget, like a {@link WToggle}.
 */
public abstract class WAbstractToggle extends WAbstractWidget {
	protected boolean toggleState = false;

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		setToggleState(!getToggleState());
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Asserts whether this toggle is on or not.
	 *
	 * @return True if on; false if off.
	 */
	public boolean getToggleState() {
		return toggleState;
	}

	/**
	 * Sets the toggle's toggled state.
	 *
	 * @param toggleState Boolean representing on (true) or off (false).
	 */
	public <W extends WAbstractToggle> W setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
		return (W) this;
	}
}
