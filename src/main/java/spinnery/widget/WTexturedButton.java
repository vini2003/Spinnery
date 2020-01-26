package spinnery.widget;

import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;

public class WTexturedButton extends WWidget {
	protected Identifier texture;
	protected Identifier textureActive;
	protected Identifier textureDisabled;

	protected boolean active = false;
	protected boolean disabled = false;

	protected WTexturedButton(WPosition position, WSize size, WInterface linkedInterface) {
		setPosition(position);
		setSize(size);
		setInterface(linkedInterface);
	}

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture) {
		this(position, size, linkedInterface, texture, null, null);
	}

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture, Identifier textureActive) {
		this(position, size, linkedInterface, texture, textureActive, null);
	}

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture, Identifier textureActive, Identifier textureDisabled) {
		this(position, size, linkedInterface);
		setTexture(texture);
		setTextureActive(textureActive);
		setTextureDisabled(textureDisabled);
	}

	public Identifier getTexture() {
		return texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
	}

	public Identifier getTextureActive() {
		return textureActive;
	}

	public void setTextureActive(Identifier textureActive) {
		this.textureActive = textureActive;
	}

	public Identifier getTextureDisabled() {
		return textureDisabled;
	}

	public void setTextureDisabled(Identifier textureDisabled) {
		this.textureDisabled = textureDisabled;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	protected Identifier getDrawTexture() {
		if (isDisabled() && getTextureDisabled() != null) {
			return textureDisabled;
		}
		if (isActive() && getTextureActive() != null) {
			return textureActive;
		}
		return texture;
	}

	@Override
	public void onMouseMoved(double mouseX, double mouseY) {
		setActive(getFocus());
        super.onMouseMoved(mouseX, mouseY);
    }

	@Override
	public void draw() {
		BaseRenderer.drawImage(getX(), getY(), getZ(), getSize().getX(), getSize().getY(), getDrawTexture());
	}
}
