package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

public class WDynamicImage extends WWidget {
	protected Identifier[] textures;

	protected int position = 0;

	public WDynamicImage(double positionX, double positionY, double positionZ, double sizeX, double sizeY, WPanel linkedWPanel, Identifier... textures) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTextures(textures);

		setLinkedWPanel(linkedWPanel);
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
	public void drawWidget() {
		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getTexture(next()));
	}
}
