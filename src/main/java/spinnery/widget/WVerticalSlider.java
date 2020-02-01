package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WVerticalSlider extends WWidget implements WClient, WFocusedMouseListener, WFocusedKeyboardListener {
	protected int limit = 0;
	protected float progress = 0;
	protected String slidTotal = "0";
	protected int slidStringPosition;

	public WVerticalSlider(WPosition position, WSize size, WInterface linkedInterface, int limit) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setLimit(limit);
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

		BaseRenderer.drawRectangle(x, y, z, sX, 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawRectangle(x, y, z, 1, (sY), getStyle().asColor("top_left.background"));

		BaseRenderer.drawRectangle(x, y + (sY) - 1, z, sX, 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawRectangle(x + sX, y, z, 1, (sY + 7), getStyle().asColor("bottom_right.background"));

		BaseRenderer.drawRectangle(x + 1, y + 1, z, sX - 1, ((sY) / l) * p - 2, getStyle().asColor("background.on"));
		BaseRenderer.drawRectangle(x + 1, y + ((sY) / l) * p - 1, z, sX - 1, (sY) - ((sY) / l) * p, getStyle().asColor("background.off"));

		BaseRenderer.drawBeveledPanel(x - 1, Math.min(y + sY - 7, y + (sY / l) * p), z, sX + 3, 8, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
	}

	public String getSlidTotal() {
		return slidTotal;
	}

	public void setSlidTotal(String slidTotal) {
		this.slidTotal = slidTotal;
	}
}
