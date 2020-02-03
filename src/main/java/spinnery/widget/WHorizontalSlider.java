package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedKeyboardListener;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

@Environment(EnvType.CLIENT)
@WFocusedKeyboardListener
@WFocusedMouseListener
public class WHorizontalSlider extends WAbstractSlider {
	protected String total = "0";
	protected int tX;

	@Override
	public void onLayoutChange() {
		tX = getX() + (getWidth() + 7) / 2 - TextRenderer.width(String.valueOf(progress)) / 2;
	}

	@Override
	protected void updatePosition(int mouseX, int mouseY) {
		double innerWidth = getInnerSize().getWidth();
		double percentComplete = Math.max(0, (mouseX - getInnerAnchor().getX()) / innerWidth);
		setProgress(min + percentComplete * (max - min));
	}

	@SuppressWarnings("unchecked")
	@Override
	public WHorizontalSlider setProgress(double progress) {
		super.setProgress(progress);
		super.setProgress(progress);
		total = String.valueOf(this.progress);
		onLayoutChange();
		return this;
	}

	@Override
	public WPosition getInnerAnchor() {
		return getPosition().add(1, 1, 0);
	}

	@Override
	public WSize getInnerSize() {
		return getSize().add(-2, -2);
	}

	@Override
	public WSize getKnobSize() {
		return WSize.of(6, getHeight() + 3);
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

		TextRenderer.pass().shadow(isLabelShadowed()).text(total).at(tX, y + sY + 4, z)
				.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();

		BaseRenderer.drawRectangle(x, y, z, (sX), 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawRectangle(x, y, z, 1, sY, getStyle().asColor("top_left.background"));

		BaseRenderer.drawRectangle(x, y + sY, z, (sX), 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawRectangle(x + (sX), y, z, 1, sY + 1, getStyle().asColor("bottom_right.background"));

		WPosition innerAnchor = getInnerAnchor();
		WSize innerSize = getInnerSize();
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

		WSize knobSize = getKnobSize();
		int knobWidth = knobSize.getWidth();
		int knobHeight = knobSize.getHeight();
		int knobX = (int) (x + (innerWidth - (double) knobWidth / 2) * percentComplete);
		int clampedX = (int) Math.min(x + innerWidth - (double) knobWidth / 2, Math.max(x, knobX));
		BaseRenderer.drawBeveledPanel(clampedX, y - 1, z, knobWidth, knobHeight,
				getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"),
				getStyle().asColor("bottom_right.foreground"));
	}
}
