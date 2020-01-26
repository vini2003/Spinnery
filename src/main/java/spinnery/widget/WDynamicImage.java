package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WDynamicImage extends WWidget implements WClient, WFocusedMouseListener {
	protected Identifier[] textures;

	protected int currentImage = 0;

	public WDynamicImage(WPosition position, WSize size, WInterface linkedInterface, Identifier... textures) {
		setInterface(linkedInterface);

		setPosition(position);

		setSize(size);

		setTextures(textures);
	}

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

	public void setCurrentImage(int currentImage) {
		this.currentImage = currentImage;
	}

	public Identifier[] getTextures() {
		return textures;
	}

	public void setTextures(Identifier... textures) {
		this.textures = textures;
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
}
