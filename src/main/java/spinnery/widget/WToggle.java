package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedMouseListener;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WToggle extends WWidget {
	protected boolean toggleState = false;

	public WToggle build() {
		return this;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		setToggleState(!getToggleState());
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawRectangle(x, y, z, sX, 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawRectangle(x, y, z, 1, sY, getStyle().asColor("top_left.background"));

		BaseRenderer.drawRectangle(x, y + sY, z, sX, 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawRectangle(x + sX, y, z, 1, sY + 1, getStyle().asColor("bottom_right.background"));

		BaseRenderer.drawRectangle(x + 1, y + 1, z, sX - 1, sY - 1, getToggleState() ? getStyle().asColor("background.on") : getStyle().asColor("background.off"));

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(x + sX - 8, y - 1, z, 8, sY + 3, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
		} else {
			BaseRenderer.drawBeveledPanel(x + 1, y - 1, z, 8, sY + 3, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
		}

		if (hasLabel()) {
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + sX + 2, y + sY / 2 - 4.5, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
		}
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public void setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
	}
}
