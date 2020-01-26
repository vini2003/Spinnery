package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class WVerticalSlider extends WWidget implements WClient, WFocusedMouseListener, WFocusedKeyboardListener {
	public static final int TOP_LEFT_BACKGROUND = 0;
	public static final int BOTTOM_RIGHT_BACKGROUND = 1;
	public static final int BACKGROUND_ON = 2;
	public static final int BACKGROUND_OFF = 3;
	public static final int TOP_LEFT_FOREGROUND = 4;
	public static final int BOTTOM_RIGHT_FOREGROUND = 5;
	public static final int FOREGROUND = 6;
	public static final int TEXT = 7;
	protected int limit = 0;
	protected float progress = 0;
	protected String slidTotal = "0";
	protected int slidStringPosition;

	public WVerticalSlider(WPosition position, WSize size, WInterface linkedInterface, int limit) {
		setInterface(linkedInterface);

		setPosition(position);

		setSize(size);

		setTheme("light");

		setLimit(limit);
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(TOP_LEFT_BACKGROUND, WColor.of(rawTheme.get("top_left_background")));
		theme.add(BOTTOM_RIGHT_BACKGROUND, WColor.of(rawTheme.get("bottom_right_background")));
		theme.add(BACKGROUND_ON, WColor.of(rawTheme.get("background_on")));
		theme.add(BACKGROUND_OFF, WColor.of(rawTheme.get("background_off")));
		theme.add(TOP_LEFT_FOREGROUND, WColor.of(rawTheme.get("top_left_foreground")));
		theme.add(BOTTOM_RIGHT_FOREGROUND, WColor.of(rawTheme.get("bottom_right_foreground")));
		theme.add(FOREGROUND, WColor.of(rawTheme.get("foreground")));
		theme.add(TEXT, WColor.of(rawTheme.get("text")));
		return theme;
	}

	public int getSlidStringPosition() {
		return slidStringPosition;
	}

	public void setSlidStringPosition(int slidStringPosition) {
		this.slidStringPosition = slidStringPosition;
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT) {
			setProgress(Math.min(getProgress() + 1, getLimit() - 1));
		}
		if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE) {
			setProgress(Math.max(getProgress() - 1, 0));
		}
		super.onKeyPressed(keyPressed, character, keyModifier);
	}

	public float getProgress() {
		return progress;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setProgress(float progress) {
		this.progress = progress;
		setSlidTotal(Integer.toString(Math.round(getProgress())));
		setSlidStringPosition(getY() + getHeight() / 2 - BaseRenderer.getTextRenderer().getStringWidth(Integer.toString((int) getProgress())) / 2);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		updatePosition(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updatePosition(int mouseX, int mouseY) {
		setProgress((mouseY - getY()) * ((float) getLimit() / (float) (getWidth())));
	}

	@Override
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		updatePosition(mouseX, mouseY);
		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int l = getLimit();
		float p = getProgress();

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawText(isLabelShadowed(), getSlidTotal(), x + sX + 4, y + sY / 2, 0xffffff);

		BaseRenderer.drawRectangle(x, y, z, sX, 1, getColor(TOP_LEFT_BACKGROUND));
		BaseRenderer.drawRectangle(x, y, z, 1, (sY), getColor(TOP_LEFT_BACKGROUND));

		BaseRenderer.drawRectangle(x, y + (sY) - 1, z, sX, 1, getColor(BOTTOM_RIGHT_BACKGROUND));
		BaseRenderer.drawRectangle(x + sX, y, z, 1, (sY + 7), getColor(BOTTOM_RIGHT_BACKGROUND));

		BaseRenderer.drawRectangle(x + 1, y + 1, z, sX - 1, ((sY) / l) * p - 2, getColor(BACKGROUND_ON));
		BaseRenderer.drawRectangle(x + 1, y + ((sY) / l) * p - 1, z, sX - 1, (sY) - ((sY) / l) * p, getColor(BACKGROUND_OFF));

		BaseRenderer.drawBeveledPanel(x - 1, Math.min(y + sY - 7, y + (sY / l) * p), z, sX + 3, 8, getColor(TOP_LEFT_FOREGROUND), getColor(FOREGROUND), getColor(BOTTOM_RIGHT_FOREGROUND));
	}

	public String getSlidTotal() {
		return slidTotal;
	}

	public void setSlidTotal(String slidTotal) {
		this.slidTotal = slidTotal;
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}
}
