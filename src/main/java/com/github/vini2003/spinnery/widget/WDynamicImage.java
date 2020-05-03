package com.github.vini2003.spinnery.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.ResourceLocation;
import com.github.vini2003.spinnery.client.BaseRenderer;

@OnlyIn(Dist.CLIENT)
public class WDynamicImage extends WAbstractWidget {
	protected ResourceLocation[] textures;

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

	public ResourceLocation[] getTextures() {
		return textures;
	}

	public <W extends WDynamicImage> W setTextures(ResourceLocation... textures) {
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
	public void draw() {
		if (isHidden()) {
			return;
		}

		BaseRenderer.drawImage(getX(), getY(), getZ(), getWidth(), getHeight(), getTexture());
	}

	public ResourceLocation getTexture() {
		return textures[currentImage];
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
