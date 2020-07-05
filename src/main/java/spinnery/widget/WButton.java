package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;

@Environment(EnvType.CLIENT)
public class WButton extends WAbstractButton {
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		//RenderSystem.translatef(0, 0, getZ() * 400f);
  		//matrices.translate(0, 0, getZ() * 400f);

		if (isLowered()) {
			BaseRenderer.drawBeveledPanel(matrices, provider, getX(), getY(), getZ(), getWidth(), getHeight(), getStyle().asColor("top_left.on"), getStyle().asColor("background.on"), getStyle().asColor("bottom_right.on"));
		} else {
			BaseRenderer.drawBeveledPanel(matrices, provider, getX(), getY(), getZ(), getWidth(), getHeight(), getStyle().asColor("top_left.off"), getStyle().asColor("background.off"), getStyle().asColor("bottom_right.off"));
		}

		if (hasLabel()) {
			TextRenderer.pass().text(getLabel()).at(getX() + (getWidth() / 2 - TextRenderer.width(getLabel()) / 2), getY() + (getHeight() / 2 - 4), getZ())
					.shadow(getStyle().asBoolean("label.shadow")).shadowColor(getStyle().asColor("label.shadow_color"))
					.color(getStyle().asColor("label.color")).render(matrices, provider);
		}

		//RenderSystem.translatef(0, 0, getZ() * -400f);
  		//matrices.translate(0, 0, getZ() * -400f);
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
