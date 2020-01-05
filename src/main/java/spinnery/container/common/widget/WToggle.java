package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;

public class WToggle extends WWidget {
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
		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), 1, 0xFF373737);
		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), 1, getSizeY(), 0xFF373737);
		BaseRenderer.drawRectangle(getPositionX() + 1, getPositionY() + 1, getPositionZ(), getSizeX() - 1, getSizeY() - 1, getState() ? 0xFF00C116 : 0xFF8b8b8b);
		BaseRenderer.drawRectangle(getPositionX(), getPositionY() + getSizeY(), getPositionZ(), getSizeX(), 1, 0xFFFFFFFF);
		BaseRenderer.drawRectangle(getPositionX() + getSizeX(), getPositionY(), getPositionZ(), 1, getSizeY() + 1, 0xFFFFFFFF);

		if (getState()) {
			BaseRenderer.drawBeveledPanel(getPositionX() + getSizeX() - 8, getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, 0xFFFFFFFF, 0xFF8b8b8b, 0xFF373737);
		} else {
			BaseRenderer.drawBeveledPanel(getPositionX() + 1, getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, 0xFFFFFFFF, 0xFF8b8b8b, 0xFF373737);
		}
	}
}
