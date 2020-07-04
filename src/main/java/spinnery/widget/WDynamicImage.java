package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.render.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WDynamicImage extends WAbstractWidget {
	protected Identifier[] textures;

	protected int currentImage = 0;

	public int next() {
		if (getCurrentImage() < getTextures().length - 1) {
			setCurrentImage(getCurrentImage() + 1);
		} else {
			setCurrentImage(0);
		}
		return getCurrentImage();
	}

	public int getCurrentImage() {
		return currentImage;
	}

	public <W extends WDynamicImage> W setCurrentImage(int currentImage) {
		this.currentImage = currentImage;
		return (W) this;
	}

	public Identifier[] getTextures() {
		return textures;
	}

	public <W extends WDynamicImage> W setTextures(Identifier... textures) {
		this.textures = textures;
		return (W) this;
	}

	public int previous() {
		if (getCurrentImage() > 0) {
			setCurrentImage(getCurrentImage() - 1);
		} else {
			setCurrentImage(getTextures().length - 1);
		}
		return getCurrentImage();
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		RenderSystem.translatef(0, 0, getZ() * 400f);
  		matrices.translate(0, 0, getZ() * 400f);

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), getZ(), getWidth(), getHeight(), getTexture());

  		matrices.translate(0, 0, getZ() * -400f);
		RenderSystem.translatef(0, 0, getZ() * -400f);
	}

	public Identifier getTexture() {
		return textures[currentImage];
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
