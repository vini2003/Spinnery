package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

/**
 * Represents a toggleable widget.
 */
public class WToggle extends WWidget {
	public static Identifier DEFAULT_ON = new Identifier("spinnery:textures/widget/toggle_on_default.png");
	public static Identifier DEFAULT_OFF = new Identifier("spinnery:textures/widget/toggle_off_default.png");

	protected Identifier texture_on = DEFAULT_ON;
	protected Identifier texture_off = DEFAULT_OFF;


	protected boolean toggled = false;

	public WToggle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
		setPosition(positionX, positionY, positionZ);
		setSize(sizeX, sizeY);

		setLinkedPanel(linkedWPanel);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (isFocused(mouseX, mouseY)) {
			setToggled(!isToggled());
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean isToggled() { return toggled; }

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public Identifier getTexture(boolean toggled) {
		return toggled ? texture_on : texture_off;
	}

	public Identifier getCurrentTexture() { return getTexture(toggled); }

	public void setTexture(boolean state, Identifier texture) {
		if (state) {
			texture_on = texture;
		} else {
			texture_off = texture;
		}
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getCurrentTexture());
	}
}
