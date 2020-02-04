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

	protected boolean isDisabled = false;

	public Identifier getInactive() {
		return inactive;
	}

	public <W extends WTexturedButton> W setInactive(Identifier inactive) {
		this.inactive = inactive;
		return (W) this;
	}

	@Override
	public void draw() {
		BaseRenderer.drawImage(getX(), getY(), getZ(), getWidth(), getHeight(), getDrawTexture());
	}

	protected Identifier getDrawTexture() {
		if (isDisabled() && getDisabled() != null) {
			return disabled;
		}
		if (isActive() && getActive() != null) {
			return active;
		}
		return inactive;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public Identifier getDisabled() {
		return disabled;
	}

	public <W extends WTexturedButton> W setDisabled(Identifier disabled) {
		this.disabled = disabled;
		return (W) this;
	}

	public boolean isActive() {
		return getFocus();
	}

	public Identifier getActive() {
		return active;
	}

	public <W extends WTexturedButton> W setActive(Identifier active) {
		this.active = active;
		return (W) this;
	}

	public <W extends WTexturedButton> W setDisabled(boolean disabled) {
		this.isDisabled = disabled;
		return (W) this;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (isDisabled) return;
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (isDisabled) return;
		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}
}
