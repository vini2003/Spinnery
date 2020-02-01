package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WHorizontalSlider extends WWidget implements WClient, WFocusedMouseListener, WFocusedKeyboardListener {
	protected int limit = 0;
	protected float progress = 0;
	protected String total = "0";
	protected int tX;

	public WHorizontalSlider(WPosition position, WSize size, WInterface linkedInterface, int limit) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setLimit(limit);
		updatePosition(getX(), getY());
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
		total = Integer.toString(Math.round(getProgress()));
		tX = getX() + (getWidth() + 7) / 2 - BaseRenderer.getTextRenderer().getStringWidth(Integer.toString((int) getProgress())) / 2;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		updatePosition(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updatePosition(int mouseX, int mouseY) {
		setProgress(((mouseX - getX()) * ((float) getLimit() / (float) (getWidth()))));
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

		BaseRenderer.drawText(isLabelShadowed(), total, tX, y + sY + 4, getStyle().asColor("text"));

		BaseRenderer.drawRectangle(x, y, z, (sX), 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawRectangle(x, y, z, 1, sY, getStyle().asColor("top_left.background"));

		BaseRenderer.drawRectangle(x, y + sY, z, (sX), 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawRectangle(x + (sX), y, z, 1, sY + 1, getStyle().asColor("bottom_right.background"));

		BaseRenderer.drawRectangle(x + 1, y + 1, z, ((sX) / l) * p - 1, sY - 1, getStyle().asColor("background.on"));
		BaseRenderer.drawRectangle(x + ((sX) / l) * p, y + 1, z, (sX) - ((sX) / l) * p, sY - 1, getStyle().asColor("background.off"));

		BaseRenderer.drawBeveledPanel(Math.min(x + sX - 7, x + (sX / l) * p), y - 1, z, 8, sY + 3, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
	}
}
