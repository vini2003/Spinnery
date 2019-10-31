package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

/**
 * Represents a static (i.e. unchanging) image widget.
 */
public class WStaticImage extends WWidget {
	protected Identifier texture;

	public WStaticImage(WPanel linkedWPanel, double positionX, double positionY, double positionZ, double sizeX, double sizeY, Identifier texture) {
		setPosition(positionX, positionY, positionZ);
		setSize(sizeX, sizeY);

		setTexture(texture);

		setLinkedPanel(linkedWPanel);
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
