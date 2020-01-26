package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WStaticImage extends WWidget implements WClient, WFocusedMouseListener {
	protected Identifier texture;

	public WStaticImage(WPosition position, WSize size, WInterface linkedInterface, Identifier texture) {
		setInterface(linkedInterface);

		setPosition(position);

		setSize(size);

		setTexture(texture);
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

	public Identifier getTexture() {
		return texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
	}
}
