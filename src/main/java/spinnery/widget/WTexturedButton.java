package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WTexturedButton extends WWidget {
	protected Identifier inactive;
	protected Identifier active;
	protected Identifier disabled;

	protected boolean isDisabledf = false;

	public WTexturedButton inactive(Identifier inactive) {
		this.inactive = inactive;
		return this;
	}

	public WTexturedButton active(Identifier active) {
		this.active = active;
		return this;
	}

	public WTexturedButton disabled(Identifier disabled) {
		this.disabled = disabled;
		return this;
	}

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture, Identifier textureActive, Identifier textureDisabled) {
		this(position, size, linkedInterface);
		setInactive(texture);
		setActive(textureActive);
		setDisabled(textureDisabled);
	}

	protected WTexturedButton(WPosition position, WSize size, WInterface linkedInterface) {
		setPosition(position);
		setSize(size);
		setInterface(linkedInterface);
	}

	public WTexturedButton(WPosition position, WSize size, WInterface linkedInterface, Identifier texture, Identifier textureActive) {
		this(position, size, linkedInterface, texture, textureActive, null);
	}

	public Identifier getInactive() {
		return inactive;
	}

	public void setInactive(Identifier inactive) {
		this.inactive = inactive;
	}

	@Override
	public void draw() {
		BaseRenderer.drawImage(getX(), getY(), getZ(), getWidth(), getHeight(), getDrawTexture());
	}

	protected Identifier getDrawTexture() {
		if (isDisabledf() && getDisabled() != null) {
			return disabled;
		}
		if (isActive() && getActive() != null) {
			return active;
		}
		return inactive;
	}

	public boolean isDisabledf() {
		return isDisabledf;
	}

	public Identifier getDisabled() {
		return disabled;
	}

	public void setDisabled(Identifier disabled) {
		this.disabled = disabled;
	}

	public boolean isActive() {
		return getFocus();
	}

	public Identifier getActive() {
		return active;
	}

	public void setActive(Identifier active) {
		this.active = active;
	}

	public void setDisabledf(boolean disabledf) {
		this.isDisabledf = disabledf;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (isDisabledf) return;
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (isDisabledf) return;
		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}
}
