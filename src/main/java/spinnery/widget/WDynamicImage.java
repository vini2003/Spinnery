package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
	public void draw() {
		if (isHidden()) {
			return;
		}

		BaseRenderer.drawImage(getX(), getY(), getZ(), getWidth(), getHeight(), getTexture());
	}

	public Identifier getTexture() {
		return textures[currentImage];
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
