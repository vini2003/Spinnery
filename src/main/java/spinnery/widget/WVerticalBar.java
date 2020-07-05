package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;
import spinnery.client.render.BaseRenderer;
import spinnery.client.utility.ScissorArea;

@Environment(EnvType.CLIENT)
public class WVerticalBar extends WAbstractBar {
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		float rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		float scale = (float) MinecraftClient.getInstance().getWindow().getScaleFactor();

		float sBGY = (((sY / limit.getValue().intValue()) * progress.getValue().intValue()));

		ScissorArea scissorArea = new ScissorArea((int) (x * scale), (int) (rawHeight - ((y + sY - sBGY) * scale)), (int) (sX * scale), (int) ((sY - sBGY) * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), z, getWidth(), getHeight(), getBackgroundTexture());

		scissorArea.destroy();

		scissorArea = new ScissorArea((int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sBGY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), z, getWidth(), getHeight(), getForegroundTexture());

		scissorArea.destroy();
	}
}
