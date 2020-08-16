package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.client.utilities.Drawings;

@Environment(EnvType.CLIENT)
public class WTextField extends WAbstractTextEditor {
	private final PartitionedTexture texture = new PartitionedTexture(Spinnery.identifier("textures/widget/text_background.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F);

	protected Integer fixedLength;

	public Integer getFixedLength() {
		return fixedLength;
	}

	@SuppressWarnings("unchecked")
	public <W extends WTextField> W setFixedLength(Integer fixedLength) {
		this.fixedLength = fixedLength;
		return (W) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <W extends WAbstractTextEditor> W setText(String text) {
		String finalText = text.replaceAll("\n", "");
		if (fixedLength != null && fixedLength >= 0 && fixedLength < finalText.length()) {
			finalText = finalText.substring(0, fixedLength);
		}
		return (W) super.setText(finalText);
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		float sX = getWidth();
		float sY = getHeight();

		texture.draw(matrices, provider, x, y, sX, sY);

		renderField(matrices, provider);
	}
}
