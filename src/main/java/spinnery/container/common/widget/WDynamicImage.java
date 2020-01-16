package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

public class WDynamicImage extends WWidget {
	protected Identifier[] textures;

	protected int position = 0;

	public WDynamicImage(WAnchor anchor, double positionX, double positionY, double positionZ, double sizeX, double sizeY, WPanel linkedWPanel, Identifier... textures) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
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


	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public Identifier getTexture(int position) {
		return textures[position];
	}

	public void setTextures(Identifier... textures) {
		this.textures = textures;
	}

	public Identifier[] getTextures() {
		return textures;
	}

	@Override
	public void draw() {
		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getTexture(next()));
	}
}
