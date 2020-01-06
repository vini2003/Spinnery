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
