package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;

@Environment(EnvType.CLIENT)
public class WButton extends WAbstractButton {
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
			int x = (TextRenderer.width(getLabel()) > getWidth() - 6) ? (getWideX() + 2) : (getX() + 3);
			TextRenderer.pass().text(getLabel()).at(x, getHighY() / 2 - 4.5, getZ())
					.shadow(getStyle().asBoolean("label.shadow")).shadowColor(getStyle().asColor("label.shadow_color"))
					.color(getStyle().asColor("label.color")).render();
		}
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
