package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WButton extends WWidget implements WClient {
	protected boolean toggleState = false;
	protected int toggleTicks = 0;
	protected WButton.Theme drawTheme;

	public WButton(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WInterface linkedPanel) {
		setInterface(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (scanFocus(mouseX, mouseY)) {
			setToggleState(!getToggleState());
			setToggleTicks(1);
		}

		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
			drawTheme = ResourceRegistry.get(getTheme()).getWButtonTheme();
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), drawTheme.getTopLeftOn(), drawTheme.getBackgroundOn(), drawTheme.getBottomRightOn());
		} else {
			BaseRenderer.drawBeveledPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), drawTheme.getTopLeftOff(), drawTheme.getBackgroundOff(), drawTheme.getBottomRightOff());
		}
		if (hasLabel()) {
			if (BaseRenderer.getTextRenderer().getStringWidth(getLabel()) > getSizeX() - 6) {
				BaseRenderer.getTextRenderer().drawWithShadow(getLabel(), (float) (getPositionX() + getSizeX() + 2), (float) (getPositionY() + getSizeY() / 2 - 4.5), drawTheme.getLabel().RGB);
			} else {
				BaseRenderer.getTextRenderer().drawWithShadow(getLabel(), (float) (getPositionX() + 3), (float) (getPositionY() + getSizeY() / 2 - 4.5), drawTheme.getLabel().RGB);
			}
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

	/**
	 * JSON loading handler class, used with GSON.
	 * Provides parsing, building and getters for this class' theme options.
	 */
	public class Theme extends WWidget.Theme {
		transient private WColor topLeftOn;
		transient private WColor bottomRightOn;
		transient private WColor backgroundOn;
		transient private WColor topLeftOff;
		transient private WColor bottomRightOff;
		transient private WColor backgroundOff;
		transient private WColor label;

		@SerializedName("top_left_on")
		private String rawTopLeftOn;

		@SerializedName("bottom_right_on")
		private String rawBottomRightOn;

		@SerializedName("background_on")
		private String rawBackgroundOn;

		@SerializedName("top_left_off")
		private String rawTopLeftOff;

		@SerializedName("bottom_right_off")
		private String rawBottomRightOff;

		@SerializedName("background_off")
		private String rawBackgroundOff;

		@SerializedName("label")
		private String rawLabel;

		public void build() {
			topLeftOn = new WColor(rawTopLeftOn);
			bottomRightOn = new WColor(rawBottomRightOn);
			backgroundOn = new WColor(rawBackgroundOn);
			topLeftOff = new WColor(rawTopLeftOff);
			bottomRightOff = new WColor(rawBottomRightOff);
			backgroundOff = new WColor(rawBackgroundOff);
			label = new WColor(rawLabel);
		}

		public WColor getTopLeftOn() {
			return topLeftOn;
		}

		public WColor getBottomRightOn() {
			return bottomRightOn;
		}

		public WColor getBackgroundOn() {
			return backgroundOn;
		}

		public WColor getTopLeftOff() {
			return topLeftOff;
		}

		public WColor getBottomRightOff() {
			return bottomRightOff;
		}

		public WColor getBackgroundOff() {
			return backgroundOff;
		}

		public WColor getLabel() {
			return label;
		}
	}
}
