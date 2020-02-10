package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedKeyboardListener;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

@Environment(EnvType.CLIENT)
@WFocusedKeyboardListener
@WFocusedMouseListener
public class WVerticalSlider extends WAbstractSlider {
	@Override
	public Position getProgressTextAnchor() {
		return Position.of(this).add(getWidth() + 4, getHeight() / 2 - TextRenderer.height() / 2, 0);
	}

	@Override
	protected void updatePosition(int mouseX, int mouseY) {
		double innerHeight = getInnerSize().getHeight();
		double percentComplete = Math.max(0, (getInnerAnchor().getY() + innerHeight - mouseY) / innerHeight);
		setProgress(min + percentComplete * (max - min));
	}

	@Override
	public Size getKnobSize() {
		return Size.of(getWidth() + 3, 6);
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

		if (isProgressVisible()) {
			Position tPos = getProgressTextAnchor();
			TextRenderer.pass().shadow(isLabelShadowed()).text(getFormattedProgress()).at(tPos.getX(), tPos.getY(), tPos.getZ())
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
		}

		BaseRenderer.drawRectangle(x, y, z, sX, 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawRectangle(x, y, z, 1, (sY), getStyle().asColor("top_left.background"));

		BaseRenderer.drawRectangle(x, y + (sY) - 1, z, sX, 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawRectangle(x + sX, y, z, 1, sY, getStyle().asColor("bottom_right.background"));

		Position innerAnchor = getInnerAnchor();
		Size innerSize = getInnerSize();
		int innerX = innerAnchor.getX();
		int innerY = innerAnchor.getY();
		int innerWidth = innerSize.getWidth();
		int innerHeight = innerSize.getHeight();
		double percentComplete = getPercentComplete();
		double percentLeft = 1 - percentComplete;
		BaseRenderer.drawRectangle(innerX, innerY + innerHeight * percentLeft, z, innerWidth, innerHeight * percentComplete,
				getStyle().asColor("background.on"));
		BaseRenderer.drawRectangle(innerX, innerY, z, innerWidth, innerHeight * percentLeft,
				getStyle().asColor("background.off"));

		Size knobSize = getKnobSize();
		int knobWidth = knobSize.getWidth();
		int knobHeight = knobSize.getHeight();
		int knobY = (int) (y + (innerHeight - (double) knobSize.getHeight() / 2) * percentLeft);
		int clampedY = (int) Math.min(y + innerHeight - (double) knobHeight / 2, Math.max(y, knobY));
		BaseRenderer.drawBeveledPanel(x - 1, clampedY, z, knobWidth, knobHeight,
				getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"),
				getStyle().asColor("bottom_right.foreground"));
	}
}
