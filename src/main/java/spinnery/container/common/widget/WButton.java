package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WButton extends WWidget {
	public class Theme {
		@SerializedName("top_left_on")
		private String topLeftOn;

		@SerializedName("bottom_right_on")
		private String bottomRightOn;

		@SerializedName("background_on")
		private String backgroundOn;

		@SerializedName("top_left_off")
		private String topLeftOff;

		@SerializedName("bottom_right_off")
		private String bottomRightOff;

		@SerializedName("background_off")
		private String backgroundOff;

		public String getTopLeftOn() {
			return topLeftOn;
		}

		public String getBottomRightOn() {
			return bottomRightOn;
		}

		public String getBackgroundOn() {
			return backgroundOn;
		}

		public String getTopLeftOff() {
			return topLeftOff;
		}

		public String getBottomRightOff() {
			return bottomRightOff;
		}

		public String getBackgroundOff() {
			return backgroundOff;
		}
	}

	protected boolean toggleState = false;
	protected int toggleTicks = 0;

	public WButton(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (scanFocus(mouseX, mouseY)) {
			setToggleState(!getToggleState());
			setToggleTicks(1);
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public void setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
	}

	public int getToggleTicks() {
		return toggleTicks;
	}

	public void setToggleTicks(int toggleTicks) {
		this.toggleTicks = toggleTicks;
	}

	public void incrementToggleTicks(int increment) {
		toggleTicks += increment;
	}

	public void decrementToggleTicks(int decrement) {
		toggleTicks -= decrement;
	}

	@Override
	public void drawWidget() {
		WButton.Theme drawTheme = ResourceRegistry.get(getTheme()).getWButtonTheme();

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), drawTheme.getTopLeftOn(), drawTheme.getBackgroundOn(), drawTheme.getBottomRightOn());
		} else {
			BaseRenderer.drawBeveledPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), drawTheme.getTopLeftOff(), drawTheme.getBackgroundOff(), drawTheme.getBottomRightOff());
		}
	}

	@Override
	public void tick() {
		if (getToggleTicks() > 0) {
			decrementToggleTicks(1);
		} else if (getToggleState()) {
			setToggleState(!getToggleState());
		}
	}
}
