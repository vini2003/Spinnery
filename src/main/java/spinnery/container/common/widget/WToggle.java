package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WToggle extends WWidget {
	public class Theme {
		@SerializedName("top_left_background")
		private String topLeftBackground;

		@SerializedName("bottom_right_background")
		private String bottomRightBackground;

		@SerializedName("background_on")
		private String backgroundOn;

		@SerializedName("background_off")
		private String backgroundOff;

		@SerializedName("top_left_foreground")
		private String topLeftForeground;

		@SerializedName("bottom_right_foreground")
		private String bottomRightForeground;

		@SerializedName("foreground")
		private String foreground;

		public String getTopLeftBackground() {
			return topLeftBackground;
		}

		public String getBottomRightBackground() {
			return bottomRightBackground;
		}

		public String getBackgroundOn() {
			return backgroundOn;
		}

		public String getBackgroundOff() {
			return backgroundOff;
		}

		public String getTopLeftForeground() {
			return topLeftForeground;
		}

		public String getBottomRightForeground() {
			return bottomRightForeground;
		}

		public String getForeground() {
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
