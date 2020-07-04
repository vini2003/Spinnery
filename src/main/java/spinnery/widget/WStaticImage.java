package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.render.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WStaticImage extends WAbstractWidget {
	protected Identifier texture;

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		RenderSystem.translatef(0, 0, getZ() * 400f);

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		BaseRenderer.drawTexturedQuad(matrices, provider, x, y, z, sX, sY, getTexture());

		RenderSystem.translatef(0, 0, getZ() * -400f);
	}

	public Identifier getTexture() {
		return texture;
	}

	public <W extends WStaticImage> W setTexture(Identifier texture) {
		this.texture = texture;
		return (W) this;
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
