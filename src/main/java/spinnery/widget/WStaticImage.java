package spinnery.widget;

import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;

public class WStaticImage extends WWidget implements WClient {
	protected Identifier texture;

	public WStaticImage(WAnchor anchor, WPosition position, WSize size, WInterface linkedInterface, Identifier texture) {
		setInterface(linkedInterface);

		setAnchor(anchor);

		setPosition(position);

		setSize(size);

		setTexture(texture);
	}

	public Identifier getTexture() {
		return texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawImage(x, y, z, sX, sY, getTexture());
	}
}
