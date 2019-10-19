package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

public class WStaticImage extends WWidget {
	protected Identifier texture;

	public WStaticImage(double positionX, double positionY, double positionZ, double sizeX, double sizeY, Identifier texture, WPanel linkedWPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTexture(texture);

		setLinkedWPanel(linkedWPanel);
	}

	public Identifier getTexture() {
		return texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getTexture());
	}
}
