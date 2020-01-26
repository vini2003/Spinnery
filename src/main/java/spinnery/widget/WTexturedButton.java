package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WTexturedButton extends WWidget {
	protected Identifier texture;
	protected Identifier textureActive;
	protected Identifier textureDisabled;

	protected boolean active = false;
	protected boolean disabled = false;

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture) {
		this(position, size, linkedInterface, texture, null, null);
	}

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture, Identifier textureActive, Identifier textureDisabled) {
		this(position, size, linkedInterface);
		setTexture(texture);
		setTextureActive(textureActive);
		setTextureDisabled(textureDisabled);
	}

	protected WTexturedButton(WPosition position, WSize size, WInterface linkedInterface) {
		setPosition(position);
		setSize(size);
		setInterface(linkedInterface);
	}

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture, Identifier textureActive) {
		this(position, size, linkedInterface, texture, textureActive, null);
	}

	public Identifier getTexture() {
		return texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
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

	protected Identifier getDrawTexture() {
		if (isDisabled() && getTextureDisabled() != null) {
			return textureDisabled;
		}
		if (isActive() && getTextureActive() != null) {
			return textureActive;
		}
		return texture;
	}

	public boolean isDisabled() {
		return disabled;
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

	public Identifier getTextureActive() {
		return textureActive;
	}

	public void setTextureActive(Identifier textureActive) {
		this.textureActive = textureActive;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
