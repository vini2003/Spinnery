package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WToggle extends WWidget implements WClient, WFocusedMouseListener {
	protected boolean toggleState = false;

	public WToggle(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
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
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX + 2, (int) (y + sY / 2 - 4.5), getStyle().asColor("label"));
		}
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public void setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
	}
}
