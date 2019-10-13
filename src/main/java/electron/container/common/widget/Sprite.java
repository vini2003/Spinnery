package electron.container.common.widget;

import electron.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

public class Sprite extends Widget {
	protected Identifier texture;

	public Sprite(int x, int y, double sizeX, double sizeY, Identifier texture, Panel linkedPanel) {
		this.positionX = x;
		this.positionY = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.texture = texture;
		this.linkedPanel = linkedPanel;
		this.alignWithContainerCenter();
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
