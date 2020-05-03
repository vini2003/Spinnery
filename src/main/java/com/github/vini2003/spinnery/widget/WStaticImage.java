package com.github.vini2003.spinnery.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.ResourceLocation;
import com.github.vini2003.spinnery.client.BaseRenderer;

@OnlyIn(Dist.CLIENT)
public class WStaticImage extends WAbstractWidget {
	protected ResourceLocation texture;

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

	public ResourceLocation getTexture() {
		return texture;
	}

	public <W extends WStaticImage> W setTexture(ResourceLocation texture) {
		this.texture = texture;
		return (W) this;
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
