package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.utilities.Sprites;

@Environment(EnvType.CLIENT)
public class WSprite extends WAbstractWidget {
	private Sprites.Sprite sprite;

	public Sprites.Sprite getSprite() {
		return sprite;
	}

	public <W extends WSprite> W setSprite(Sprites.Sprite sprite) {
		this.sprite = sprite;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		getSprite().draw(matrices, provider, getX(), getY(), getWidth(), getHeight(), false);
	}
}
