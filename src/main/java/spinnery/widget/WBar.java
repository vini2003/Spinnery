package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.lwjgl.opengl.GL11;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WBar extends WWidget {
	protected MutableInt limit = new MutableInt(0);
	protected MutableFloat progress = new MutableFloat(0);

	protected Identifier backgroundTexture;
	protected Identifier foregroundTexture;

	public WBar(WPosition position, WSize size, WInterface linkedInterface, MutableInt limit) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setLimit(limit);
	}

	public MutableInt getLimit() {
		return limit;
	}

	public void setLimit(MutableInt limit) {
		this.limit = limit;
	}

	public MutableFloat getProgress() {
		return progress;
	}

	public void setProgress(MutableFloat progress) {
		this.progress = progress;
	}

	public Identifier getBackgroundTexture() {
		return getStyle().asIdentifier("background");
	}

	public void setBackgroundTexture(Identifier backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}

	public Identifier getForegroundTexture() {
		return getStyle().asIdentifier("foreground");
	}

	public void setForegroundTexture(Identifier foregroundTexture) {
		this.foregroundTexture = foregroundTexture;
	}

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

		int sBGY = (int) ((((float) sY / limit.getValue()) * progress.getValue()));

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY - sBGY) * scale)), (int) (sX * scale), (int) ((sY - sBGY) * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getSize().getX(), getSize().getY(), getBackgroundTexture());

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sBGY * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getSize().getX(), getSize().getY(), getForegroundTexture());

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}
