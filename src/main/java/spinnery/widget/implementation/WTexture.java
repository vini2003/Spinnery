package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.utilities.Drawings;

@Environment(EnvType.CLIENT)
public class WTexture extends WAbstractWidget {
	protected Identifier texture;

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		float sX = getWidth();
		float sY = getHeight();

		Drawings.drawTexturedQuad(matrices, provider, x, y, sX, sY, getTexture());
	}

	public Identifier getTexture() {
		return texture;
	}

	public <W extends WTexture> W setTexture(Identifier texture) {
		this.texture = texture;
		return (W) this;
	}
}
