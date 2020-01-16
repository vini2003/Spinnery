package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WToggle extends WWidget {
	public class Theme extends WWidget.Theme {
		transient private WColor topLeftBackground;
		transient private WColor bottomRightBackground;
		transient private WColor backgroundOn;
		transient private WColor backgroundOff;
		transient private WColor topLeftForeground;
		transient private WColor bottomRightForeground;
		transient private WColor foreground;

		@SerializedName("top_left_background")
		private String rawTopLeftBackground;

		@SerializedName("bottom_right_background")
		private String rawBottomRightBackground;

		@SerializedName("background_on")
		private String rawBackgroundOn;

		@SerializedName("background_off")
		private String rawBackgroundOff;

		@SerializedName("top_left_foreground")
		private String rawTopLeftForeground;

		@SerializedName("bottom_right_foreground")
		private String rawBottomRightForeground;

		@SerializedName("foreground")
		private String rawForeground;

		public void build() {
			topLeftBackground = new WColor(rawTopLeftBackground);
			bottomRightBackground = new WColor(rawBottomRightBackground);
			backgroundOn = new WColor(rawBackgroundOn);
			backgroundOff = new WColor(rawBackgroundOff);
			topLeftForeground = new WColor(rawTopLeftForeground);
			bottomRightForeground = new WColor(rawBottomRightForeground);
			foreground = new WColor(rawForeground);
		}

		public WColor getTopLeftBackground() {
			return topLeftBackground;
		}

		public WColor getBottomRightBackground() {
			return bottomRightBackground;
		}

		public WColor getBackgroundOn() {
			return backgroundOn;
		}

		public WColor getBackgroundOff() {
			return backgroundOff;
		}

		public WColor getTopLeftForeground() {
			return topLeftForeground;
		}

		public WColor getBottomRightForeground() {
			return bottomRightForeground;
		}

		public WColor getForeground() {
			return foreground;
		}
	}

	protected boolean toggleState = false;

	public WToggle(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
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
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public void setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
	}

	@Override
	public void drawWidget() {
		WToggle.Theme drawTheme = ResourceRegistry.get(getTheme()).getWToggleTheme();

		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), 1, drawTheme.getTopLeftBackground());
		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), 1, getSizeY(), drawTheme.getTopLeftBackground());

		BaseRenderer.drawRectangle(getPositionX(), getPositionY() + getSizeY(), getPositionZ(), getSizeX(), 1, drawTheme.getBottomRightBackground());
		BaseRenderer.drawRectangle(getPositionX() + getSizeX(), getPositionY(), getPositionZ(), 1, getSizeY() + 1, drawTheme.getBottomRightBackground());

		BaseRenderer.drawRectangle(getPositionX() + 1, getPositionY() + 1, getPositionZ(), getSizeX() - 1, getSizeY() - 1, getToggleState() ? drawTheme.getBackgroundOn() : drawTheme.getBackgroundOff());

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(getPositionX() + getSizeX() - 8, getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, drawTheme.getTopLeftForeground(), drawTheme.getForeground(), drawTheme.getBottomRightForeground());
		} else {
			BaseRenderer.drawBeveledPanel(getPositionX() + 1, getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, drawTheme.getTopLeftForeground(), drawTheme.getForeground(), drawTheme.getBottomRightForeground());
		}
	}
}
