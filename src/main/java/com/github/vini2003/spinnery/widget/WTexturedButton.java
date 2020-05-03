package com.github.vini2003.spinnery.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.ResourceLocation;
import com.github.vini2003.spinnery.client.BaseRenderer;

@OnlyIn(Dist.CLIENT)
public class WTexturedButton extends WAbstractWidget {
	protected ResourceLocation inactive;
	protected ResourceLocation active;
	protected ResourceLocation disabled;

	protected boolean isDisabled = false;

	public ResourceLocation getInactive() {
		return inactive;
	}

	public <W extends WTexturedButton> W setInactive(ResourceLocation inactive) {
		this.inactive = inactive;
		return (W) this;
	}

	@Override
	public void draw() {
		BaseRenderer.drawImage(getX(), getY(), getZ(), getWidth(), getHeight(), getDrawTexture());
	}

	protected ResourceLocation getDrawTexture() {
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

	public ResourceLocation getDisabled() {
		return disabled;
	}

	public <W extends WTexturedButton> W setDisabled(ResourceLocation disabled) {
		this.disabled = disabled;
		return (W) this;
	}

	public <W extends WTexturedButton> W setDisabled(boolean disabled) {
		this.isDisabled = disabled;
		return (W) this;
	}

	public boolean isActive() {
		return isFocused();
	}

	public ResourceLocation getActive() {
		return active;
	}

	public <W extends WTexturedButton> W setActive(ResourceLocation active) {
		this.active = active;
		return (W) this;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (isDisabled) return;
		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (isDisabled) return;
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
