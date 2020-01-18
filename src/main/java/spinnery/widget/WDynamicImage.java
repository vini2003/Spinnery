package spinnery.widget;

import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;

public class WDynamicImage extends WWidget {
	protected Identifier[] textures;

	protected int position = 0;

	public WDynamicImage(WAnchor anchor, double positionX, double positionY, double positionZ, double sizeX, double sizeY, WPanel linkedPanel, Identifier... textures) {
		setLinkedPanel(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTextures(textures);
	}

	public int next() {
		if (getPosition() < getTextures().length - 1) {
			setPosition(getPosition() + 1);
		} else {
			setPosition(0);
		}
		return getPosition();
	}

	public int previous() {
		if (getPosition() > 0) {
			setPosition(getPosition() - 1);
		} else {
			setPosition(getTextures().length - 1);
		}
		return getPosition();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Identifier getTexture(int position) {
		return textures[position];
	}

	public Identifier[] getTextures() {
		return textures;
	}

	public void setTextures(Identifier... textures) {
		this.textures = textures;
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getTexture(next()));
	}
}
