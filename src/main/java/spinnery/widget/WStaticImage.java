package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.WFocusedMouseListener;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WStaticImage extends WWidget {
	protected Identifier texture;

	public WStaticImage texture(Identifier texture) {
		this.texture = texture;
		return this;
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
