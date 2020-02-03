package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedMouseListener;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WButton extends WWidget {
	protected boolean lowered = false;
	protected int ticks = 0;

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
			int x = (TextRenderer.width(getLabel()) > getWidth() - 6) ? (getX() + getWidth() + 2) : (getX() + 3);
			TextRenderer.pass().text(getLabel()).at(x, getY() + getHeight() / 2 - 4.5, getZ())
					.shadow(getStyle().asBoolean("label.shadow")).shadowColor(getStyle().asColor("label.shadow_color"))
					.color(getStyle().asColor("label.color")).render();
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
