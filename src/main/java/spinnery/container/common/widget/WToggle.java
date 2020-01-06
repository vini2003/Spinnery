package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WToggle extends WWidget {
	public class Theme {
		@SerializedName("top_left")
		private String topleft;

		@SerializedName("bottom_right")
		private String bottomright;

		@SerializedName("on")
		private String on;

		@SerializedName("off")
		private String off;

		public String getTopleft() {
			return topleft;
		}

		public String getBottomright() {
			return bottomright;
		}

		public String getOn() {
			return on;
		}

		public String getOff() {
			return off;
		}
	}
	protected boolean state = false;

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
			setState(!getState());
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public void drawWidget() {
		WToggle.Theme drawTheme = ResourceRegistry.get(getTheme()).getWToggleTheme();

		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), 1, drawTheme.getTopleft());
		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), 1, getSizeY(), drawTheme.getTopleft());
		BaseRenderer.drawRectangle(getPositionX() + 1, getPositionY() + 1, getPositionZ(), getSizeX() - 1, getSizeY() - 1, getState() ? drawTheme.getOn() : drawTheme.getOff());
		BaseRenderer.drawRectangle(getPositionX(), getPositionY() + getSizeY(), getPositionZ(), getSizeX(), 1, drawTheme.getBottomright());
		BaseRenderer.drawRectangle(getPositionX() + getSizeX(), getPositionY(), getPositionZ(), 1, getSizeY() + 1, drawTheme.getBottomright());

		if (getState()) {
			BaseRenderer.drawBeveledPanel(getPositionX() + getSizeX() - 8, getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, drawTheme.getOn(), drawTheme.getOff(), drawTheme.getTopleft());
		} else {
			BaseRenderer.drawBeveledPanel(getPositionX() + 1, getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, drawTheme.getOn(), drawTheme.getOff(), drawTheme.getTopleft());
		}
	}
}
