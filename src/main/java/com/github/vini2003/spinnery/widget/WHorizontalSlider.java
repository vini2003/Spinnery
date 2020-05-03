package com.github.vini2003.spinnery.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.github.vini2003.spinnery.client.BaseRenderer;
import com.github.vini2003.spinnery.client.TextRenderer;
import com.github.vini2003.spinnery.widget.api.Position;
import com.github.vini2003.spinnery.widget.api.Size;

@OnlyIn(Dist.CLIENT)
public class WHorizontalSlider extends WAbstractSlider {
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

		BaseRenderer.drawRectangle(x, y, z, (sX), 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawRectangle(x, y, z, 1, sY, getStyle().asColor("top_left.background"));

		BaseRenderer.drawRectangle(x, y + sY, z, (sX), 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawRectangle(x + (sX), y, z, 1, sY + 1, getStyle().asColor("bottom_right.background"));

		Position innerAnchor = getInnerAnchor();
		Size innerSize = getInnerSize();
		int innerX = innerAnchor.getX();
		int innerY = innerAnchor.getY();
		int innerWidth = innerSize.getWidth();
		int innerHeight = innerSize.getHeight();
		double percentComplete = getPercentComplete();
		double percentLeft = 1 - percentComplete;
		BaseRenderer.drawRectangle(innerX, innerY, z, innerWidth * percentComplete, innerHeight,
				getStyle().asColor("background.on"));
		BaseRenderer.drawRectangle(innerX + innerWidth * percentComplete, innerY, z, innerWidth * percentLeft, innerHeight,
				getStyle().asColor("background.off"));

		Size knobSize = getKnobSize();
		int knobWidth = knobSize.getWidth();
		int knobHeight = knobSize.getHeight();
		int knobX = (int) (x + (innerWidth - (double) knobWidth / 2) * percentComplete);
		int clampedX = (int) Math.min(x + innerWidth - (double) knobWidth / 2, Math.max(x, knobX));
		BaseRenderer.drawBeveledPanel(clampedX, y - 1, z, knobWidth, knobHeight,
				getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"),
				getStyle().asColor("bottom_right.foreground"));
	}

	@Override
	public Position getProgressTextAnchor() {
		String formatted = getFormattedProgress();
		return Position.of(this).add((getWidth() + 5) / 2 - TextRenderer.width(formatted) / 2, getHeight() + 4, 0);
	}

	@Override
	public Size getKnobSize() {
		return Size.of(6, getHeight() + 3);
	}

	@Override
	protected void updatePosition(int mouseX, int mouseY) {
		double innerWidth = getInnerSize().getWidth();
		double percentComplete = Math.max(0, (mouseX - getInnerAnchor().getX()) / innerWidth);
		setProgress(min + percentComplete * (max - min));
	}
}
