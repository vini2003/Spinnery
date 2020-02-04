package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.Mutable;
import org.lwjgl.opengl.GL11;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WVerticalBar extends WAbstractBar {
	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ() + 25;

		int sX = getWidth();
		int sY = getHeight();

		int rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();

		int sBGY = (int) ((((float) sY / limit.getValue().intValue()) * progress.getValue().intValue()));

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY - sBGY) * scale)), (int) (sX * scale), (int) ((sY - sBGY) * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getWidth(), getHeight(), getBackgroundTexture());

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sBGY * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getWidth(), getHeight(), getForegroundTexture());

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}
