package electron.container.common.widget;

import electron.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

public class Toggle extends Widget {
	protected Identifier texture_on = new Identifier("electron:textures/widget/toggle_on_default.png");
	protected Identifier texture_off = new Identifier("electron:textures/widget/toggle_off_default.png");

	protected boolean state = false;

	protected Identifier texture = texture_off;

	public Toggle(int x, int y, double sizeX, double sizeY, Panel linkedPanel) {
		this.positionX = x;
		this.positionY = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.linkedPanel = linkedPanel;
		this.alignWithContainerCenter();
	}

	public Toggle(int x, int y, double sizeX, double sizeY, Identifier button_on, Identifier button_off, Panel linkedPanel) {
		this.positionX = x;
		this.positionY = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.texture_on = button_on;
		this.texture_off = button_off;
		this.linkedPanel = linkedPanel;
		this.alignWithContainerCenter();
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getFocus()) {
			state = !state;
			if (state) {
				setTexture(texture_on);
			} else {
				setTexture(texture_off);
			}
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public Identifier getTexture() {
		return texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void draw() {
		BaseRenderer.drawSprite(getPositionX(), getPositionY(), getSizeX(), getSizeY(), texture, 0,0,1,1,0xFFFFFFFF);
	}
}
