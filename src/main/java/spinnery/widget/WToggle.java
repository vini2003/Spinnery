package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;

@Environment(EnvType.CLIENT)
public class WToggle extends WAbstractToggle {
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

		BaseRenderer.drawQuad(matrices, provider, x, y, z, sX, 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawQuad(matrices, provider, x, y, z, 1, sY, getStyle().asColor("top_left.background"));

		BaseRenderer.drawQuad(matrices, provider, x, y + sY, z, sX, 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawQuad(matrices, provider, x + sX, y, z, 1, sY + 1, getStyle().asColor("bottom_right.background"));

		BaseRenderer.drawQuad(matrices, provider, x + 1, y + 1, z, sX - 1, sY - 1, getToggleState() ? getStyle().asColor("background.on") : getStyle().asColor("background.off"));

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(matrices, provider, x + sX - 8, y - 1, z, 8, sY + 3, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
		} else {
			BaseRenderer.drawBeveledPanel(matrices, provider, x + 1, y - 1, z, 8, sY + 3, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
		}

		if (hasLabel()) {
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + sX + 2, y + sY / 2 - 4.5, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render(matrices, provider);
		}

		super.draw(matrices, provider);
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
