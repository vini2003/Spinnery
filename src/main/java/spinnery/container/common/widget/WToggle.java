package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

public class WToggle extends WWidget {
	protected Identifier texture_on = new Identifier("spinnery:textures/widget/toggle_on_default.png");
	protected Identifier texture_off = new Identifier("spinnery:textures/widget/toggle_off_default.png");

	protected boolean state = false;

	public WToggle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLinkedPanel(linkedWPanel);
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

	public Identifier getTexture(boolean state) {
		return state ? texture_on : texture_off;
	}

	public void setTexture(boolean state, Identifier texture) {
		if (state) {
			texture_on = texture;
		} else {
			texture_off = texture;
		}
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawRectangle(positionX, positionY, positionZ, sizeX, 1, 0xFF373737);
		BaseRenderer.drawRectangle(positionX, positionY, positionZ, 1, sizeY, 0xFF373737);
		BaseRenderer.drawRectangle(positionX + 1, positionY + 1, positionZ, sizeX - 1, sizeY - 1, getState() ? 0xFF00C116 : 0xFF8b8b8b);
		BaseRenderer.drawRectangle(positionX, positionY + sizeY, positionZ, sizeX, 1, 0xFFFFFFFF);
		BaseRenderer.drawRectangle(positionX + sizeX, positionY, positionZ, 1, sizeY + 1, 0xFFFFFFFF);

		if (getState()) {
			BaseRenderer.drawBeveledPanel(positionX + sizeX - 8, positionY - 1, positionZ, 8, sizeY + 3, 0xFFFFFFFF, 0xFF8b8b8b, 0xFF373737);
		} else {
			BaseRenderer.drawBeveledPanel(positionX + 1, positionY - 1, positionZ, 8, sizeY + 3, 0xFFFFFFFF, 0xFF8b8b8b, 0xFF373737);

		}
	}
}
