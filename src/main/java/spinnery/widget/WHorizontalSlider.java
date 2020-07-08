package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

@Environment(EnvType.CLIENT)
public class WHorizontalSlider extends WAbstractSlider {
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		if (isProgressVisible()) {
			Position tPos = getProgressTextAnchor();
			TextRenderer.pass().shadow(isLabelShadowed()).text(getFormattedProgress()).at(tPos.getX(), tPos.getY(), tPos.getZ())
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render(matrices, provider);
		}

		BaseRenderer.drawQuad(matrices, provider, x, y, z, (sX), 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawQuad(matrices, provider, x, y, z, 1, sY, getStyle().asColor("top_left.background"));

		BaseRenderer.drawQuad(matrices, provider, x, y + sY, z, (sX), 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawQuad(matrices, provider, x + (sX), y, z, 1, sY + 1, getStyle().asColor("bottom_right.background"));

		Position innerAnchor = getInnerAnchor();

		Size innerSize = getInnerSize();

		float innerX = innerAnchor.getX();
		float innerY = innerAnchor.getY();
		float innerWidth = innerSize.getWidth();
		float innerHeight = innerSize.getHeight();
		float percentComplete = getPercentComplete();
		float percentLeft = 1 - percentComplete;

		BaseRenderer.drawQuad(matrices, provider, innerX, innerY, z, innerWidth * percentComplete, innerHeight,
				getStyle().asColor("background.on"));
		BaseRenderer.drawQuad(matrices, provider, innerX + innerWidth * percentComplete, innerY, z, innerWidth * percentLeft, innerHeight,
				getStyle().asColor("background.off"));

		Size knobSize = getKnobSize();

		float knobWidth = knobSize.getWidth();
		float knobHeight = knobSize.getHeight();
		float knobX = (x + (innerWidth - knobWidth / 2f) * percentComplete);
		float clampedX = Math.min(x + innerWidth - knobWidth / 2f, Math.max(x, knobX));

		BaseRenderer.drawBeveledPanel(matrices, provider, clampedX, y - 1, z, knobWidth, knobHeight,
				getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"),
				getStyle().asColor("bottom_right.foreground"));

		super.draw(matrices, provider);
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
	protected void updatePosition(float mouseX, float mouseY) {
		float innerWidth = getInnerSize().getWidth();
		float percentComplete = Math.max(0, (mouseX - getInnerAnchor().getX()) / innerWidth);
		setProgress(min + percentComplete * (max - min));
	}
}
