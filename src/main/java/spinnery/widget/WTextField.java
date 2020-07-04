package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WTextField extends WAbstractTextEditor {
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
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		RenderSystem.translatef(0, 0, getZ() * 400f);
  		matrices.translate(0, 0, getZ() * 400f);

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		BaseRenderer.drawBeveledPanel(matrices, provider, x, y, z, sX, sY, getStyle().asColor("top_left"), getStyle().asColor("background"), getStyle().asColor("bottom_right"));

		renderField(matrices, provider);

  		matrices.translate(0, 0, getZ() * -400f);
		RenderSystem.translatef(0, 0, getZ() * -400f);
	}
}
