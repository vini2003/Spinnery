package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Texts;
import spinnery.common.utilities.miscellaneous.Position;
import spinnery.common.utilities.miscellaneous.Size;

@Environment(EnvType.CLIENT)
public class WVerticalSlider extends WAbstractSlider {
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		float sX = getWidth();
		float sY = getHeight();

		if (isProgressVisible()) {
			Position textAnchor = getProgressTextAnchor();
			Texts.pass().shadow(isLabelShadowed()).text(getFormattedProgress()).at(textAnchor.getX(), textAnchor.getY()).color(getStyle().asColor("label.color")).render(matrices, provider);
		}

		Drawings.drawQuad(matrices, provider, x, y, sX, 1, getStyle().asColor("top_left.background"));
		Drawings.drawQuad(matrices, provider, x, y, 1, (sY), getStyle().asColor("top_left.background"));

		Drawings.drawQuad(matrices, provider, x, y + (sY) - 1, sX, 1, getStyle().asColor("bottom_right.background"));
		Drawings.drawQuad(matrices, provider, x + sX, y, 1, sY, getStyle().asColor("bottom_right.background"));

		Position innerAnchor = getInnerAnchor();

		Size innerSize = getInnerSize();

		float innerX = innerAnchor.getX();
		float innerY = innerAnchor.getY();
		float innerWidth = innerSize.getWidth();
		float innerHeight = innerSize.getHeight();
		float percentComplete = getPercentComplete();
		float percentLeft = 1 - percentComplete;

		Drawings.drawQuad(matrices, provider, innerX, innerY + innerHeight * percentLeft, innerWidth, innerHeight * percentComplete, getStyle().asColor("background.on"));
		Drawings.drawQuad(matrices, provider, innerX, innerY, innerWidth, innerHeight * percentLeft, getStyle().asColor("background.off"));

		Size knobSize = getKnobSize();

		float knobWidth = knobSize.getWidth();
		float knobHeight = knobSize.getHeight();
		float knobY = (y + (innerHeight - knobSize.getHeight() / 2f) * percentLeft);
		float clampedY = Math.min(y + innerHeight - knobHeight / 2f, Math.max(y, knobY));

		Drawings.drawBeveledPanel(matrices, provider, x - 1, clampedY, knobWidth, knobHeight, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
	}

	@Override
	public Position getProgressTextAnchor() {
		return Position.of(this).add(getWidth() + 4, getHeight() / 2F - Texts.height() / 2F);
	}

	@Override
	public Size getKnobSize() {
		return Size.of(getWidth() + 3, 6);
	}

	@Override
	protected void updatePosition(float mouseX, float mouseY) {
		float innerHeight = getInnerSize().getHeight();
		float percentComplete = Math.max(0, (getInnerAnchor().getY() + innerHeight - mouseY) / innerHeight);
		setProgress(getMinimum()+ percentComplete * (getMaximum() - getMinimum()));
	}
}
