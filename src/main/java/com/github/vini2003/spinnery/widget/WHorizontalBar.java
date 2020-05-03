package com.github.vini2003.spinnery.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import com.github.vini2003.spinnery.client.BaseRenderer;

@OnlyIn(Dist.CLIENT)
public class WHorizontalBar extends WAbstractBar {
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

		int rawHeight = Minecraft.getInstance().getMainWindow().getHeight();
		double scale = Minecraft.getInstance().getMainWindow().getGuiScaleFactor();

		int sBGX = (int) ((((float) sX / limit.getValue().intValue()) * progress.getValue().intValue()));

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sY * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getWidth(), getHeight(), getBackgroundTexture());

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sBGX * scale), (int) (sY * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getWidth(), getHeight(), getForegroundTexture());

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}
