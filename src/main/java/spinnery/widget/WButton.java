package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.registry.ThemeRegistry;

@Environment(EnvType.CLIENT)
public class WButton extends WWidget implements WClient, WFocusedMouseListener {
	protected boolean lowered = false;
	protected int ticks = 0;

	public WButton(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		setLowered(true);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		if (isLowered()) {
			BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getStyle().asColor("top_left.on"), getStyle().asColor("background.on"), getStyle().asColor("bottom_right.on"));
		} else {
			BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getStyle().asColor("top_left.off"), getStyle().asColor("background.off"), getStyle().asColor("bottom_right.off"));
		}

		if (hasLabel()) {
			if (BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) > getWidth() - 6) {
				BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), (getX() + getWidth() + 2), (int) (getY() + getHeight() / 2 - 4.5), getStyle().asColor("label"));
			} else {
				BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), (getX() + 3), (int) (getY() + getHeight() / 2 - 4.5), getStyle().asColor("label"));
			}
		}
	}

	@Override
	public void tick() {
		if (isLowered() && ticks == 0) {
			setLowered(false);
		} else if (isLowered() && ticks > 0) {
			--ticks;
		}
	}

	public boolean isLowered() {
		return lowered;
	}

	public void setLowered(boolean toggleState) {
		this.lowered = toggleState;
		this.ticks = toggleState ? 2 : 0;
	}
}
