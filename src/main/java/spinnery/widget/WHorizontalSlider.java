package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Texts;
import spinnery.common.utilities.Positions;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

@Environment(EnvType.CLIENT)
public class WHorizontalSlider extends WAbstractSlider {
	private final PartitionedTexture textureScrollbar = new PartitionedTexture(Spinnery.identifier("textures/widget/slider.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.16666666666666666667F);
	private final PartitionedTexture textureScroller = new PartitionedTexture(Spinnery.identifier("textures/widget/knob.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F);
	private final PartitionedTexture textureScrollerFocus = new PartitionedTexture(Spinnery.identifier("textures/widget/knob_focus.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F);

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		if (isProgressVisible()) {
			Position textAnchor = getProgressTextAnchor();
			Texts.pass().shadow(isLabelShadowed()).text(getFormattedProgress()).at(textAnchor.getX(), textAnchor.getY()).color(getStyle().asColor("label.color")).render(matrices, provider);
		}

		Position innerAnchor = getInnerAnchor();

		Size innerSize = getInnerSize();

		float innerX = innerAnchor.getX();
		float innerY = innerAnchor.getY();
		float innerWidth = innerSize.getWidth();
		float innerHeight = innerSize.getHeight();
		float percentComplete = getPercentComplete();

		textureScrollbar.draw(matrices, provider, innerX, innerY, innerWidth * percentComplete, innerHeight);

		Size knobSize = getKnobSize();

		float knobWidth = knobSize.getWidth();
		float knobHeight = knobSize.getHeight();
		float knobX = (x + (innerWidth - knobWidth / 2f) * percentComplete);
		float clampedX = Math.min(x + innerWidth - knobWidth / 2f, Math.max(x, knobX));

		if (Positions.getMouseX() > knobX && Positions.getMouseX() < knobX + knobWidth && isWithinBounds(Positions.getMouseX(), Positions.getMouseY())) {
			textureScrollerFocus.draw(matrices, provider, clampedX, y - 1, knobWidth, knobHeight);
		} else {
			textureScroller.draw(matrices, provider, clampedX, y - 1, knobWidth, knobHeight);
		}
	}

	@Override
	public Position getProgressTextAnchor() {
		String formatted = getFormattedProgress();
		return Position.of(this).add((getWidth() + 5) / 2 - Texts.width(formatted) / 2F, getHeight() + 4);
	}

	@Override
	public Size getKnobSize() {
		return Size.of(6, getHeight() + 3);
	}

	@Override
	protected void updatePosition(float mouseX, float mouseY) {
		float innerWidth = getInnerSize().getWidth();
		float percentComplete = Math.max(0, (mouseX - getInnerAnchor().getX()) / innerWidth);
		setProgress(getMinimum() + percentComplete * (getMaximum() - getMinimum()));
	}
}
