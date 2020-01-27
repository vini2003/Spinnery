package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.lwjgl.opengl.GL11;
import spinnery.Spinnery;
import spinnery.client.BaseRenderer;
import spinnery.registry.ThemeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class WBar extends WWidget {
	protected MutableInt limit = new MutableInt(0);
	protected MutableFloat progress = new MutableFloat(0);

	protected Identifier backgroundTexture;
	protected Identifier foregroundTexture;

	public static final int BACKGROUND_TEXTURE = 0;
	public static final int FOREGROUND_TEXTURE = 1;


	public WBar(WPosition position, WSize size, WInterface linkedInterface, MutableInt limit) {
		setInterface(linkedInterface);

		setPosition(position);

		setSize(size);

		setTheme("light");

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
		return new Identifier((String) ThemeRegistry.get(getTheme(), getClass()).get(BACKGROUND_TEXTURE));
	}

	public void setBackgroundTexture(Identifier backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}

	public Identifier getForegroundTexture() {
		return new Identifier((String) ThemeRegistry.get(getTheme(), getClass()).get(FOREGROUND_TEXTURE));

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

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.put(BACKGROUND_TEXTURE, rawTheme.get("background"));
		theme.put(FOREGROUND_TEXTURE, rawTheme.get("foreground"));
		return theme;
	}
}
