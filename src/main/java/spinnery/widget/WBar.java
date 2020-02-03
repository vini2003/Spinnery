package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.Mutable;
import org.lwjgl.opengl.GL11;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WBar extends WWidget {
	protected Mutable<Number> limit;
	protected Mutable<Number> progress;

	protected Identifier backgroundTexture;
	protected Identifier foregroundTexture;

	public WBar limit(Mutable<Number> limit) {
		this.limit = limit;
		return this;
	}

	public WBar progress(Mutable<Number> progress) {
		this.progress = progress;
		return this;
	}

	public WBar foreground(Identifier foregroundTexture) {
		this.foregroundTexture = foregroundTexture;
		return this;
	}

	public WBar background(Identifier backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
		return this;
	}

	public Mutable<Number> getLimit() {
		return limit;
	}

	public void setLimit(Mutable<Number> limit) {
		this.limit = limit;
	}

	public Mutable<Number> getProgress() {
		return progress;
	}

	public void setProgress(Mutable<Number> progress) {
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

		int sBGY = (int) ((((float) sY / limit.getValue().intValue()) * progress.getValue().intValue()));

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY - sBGY) * scale)), (int) (sX * scale), (int) ((sY - sBGY) * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getWidth(), getHeight(), getBackgroundTexture());

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sBGY * scale));

		BaseRenderer.drawImage(getX(), getY(), z, getWidth(), getHeight(), getForegroundTexture());

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}
