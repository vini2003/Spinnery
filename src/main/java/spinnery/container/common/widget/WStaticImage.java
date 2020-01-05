package spinnery.container.common.widget;

import net.minecraft.util.Identifier;
import spinnery.container.client.BaseRenderer;

public class WStaticImage extends WWidget {
	protected Identifier texture;

	public WStaticImage(WAnchor anchor, double positionX, double positionY, double positionZ, double sizeX, double sizeY, Identifier texture, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTexture(texture);
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
