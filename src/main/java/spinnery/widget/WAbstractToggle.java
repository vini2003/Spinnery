package spinnery.widget;


  general toggle-like widget, like a {@link WToggle}.
 */
public abstract class WAbstractToggle extends WAbstractWidget {
	protected boolean toggleState = false;

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
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
