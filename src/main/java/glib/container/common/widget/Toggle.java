package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

public class Toggle extends Widget {
	protected Identifier texture_on = new Identifier("glib:textures/widget/toggle_on_default.png");
	protected Identifier texture_off = new Identifier("glib:textures/widget/toggle_off_default.png");

	protected boolean state = false;

	public Toggle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, Panel linkedPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLinkedPanel(linkedPanel);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (isFocused(mouseX, mouseY)) {
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
		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getTexture(getState()));
	}
}
