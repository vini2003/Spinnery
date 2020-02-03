package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.mutable.Mutable;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedKeyboardListener;
import spinnery.widget.api.WFocusedMouseListener;

@Environment(EnvType.CLIENT)
@WFocusedKeyboardListener
@WFocusedMouseListener
public class WHorizontalSlider extends WWidget {
	protected Mutable<Number> limit;
	protected Mutable<Number> progress;

	protected String total = "0";
	protected int tX;

	public WHorizontalSlider limit(Mutable<Number> limit) {
		this.limit = limit;
		return this;
	}

	public WHorizontalSlider progress(Mutable<Number> progress) {
		this.progress = progress;
		return this;
	}

	public WHorizontalSlider build() {
		updatePosition(getX(), getY());
		return this;
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT) {
			progress.setValue(Math.min(getProgress().getValue().intValue() + 1, getLimit().getValue().intValue() - 1));
		}
		if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE) {
			progress.setValue(Math.max(getProgress().getValue().intValue() - 1, 0));
		}
		super.onKeyPressed(keyPressed, character, keyModifier);
	}

	public Mutable<Number> getProgress() {
		return progress;
	}

	public Mutable<Number> getLimit() {
		return limit;
	}

	public void setLimit(Mutable<Number> limit) {
		this.limit = limit;
	}

	public void setProgress(Mutable<Number> progress) {
		this.progress = progress;
		total = Integer.toString(Math.round(getProgress().getValue().floatValue()));
		tX = getX() + (getWidth() + 7) / 2 - TextRenderer.width(String.valueOf(getProgress().getValue().intValue())) / 2;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		updatePosition(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updatePosition(int mouseX, int mouseY) {
		progress.setValue((mouseX - getX()) * (getLimit().getValue().floatValue() / (float) (getWidth())));
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

		int l = getLimit().getValue().intValue();
		float p = getProgress().getValue().floatValue();

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		TextRenderer.pass().shadow(isLabelShadowed()).text(total).at(tX, y + sY + 4, z)
				.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();

		BaseRenderer.drawRectangle(x, y, z, (sX), 1, getStyle().asColor("top_left.background"));
		BaseRenderer.drawRectangle(x, y, z, 1, sY, getStyle().asColor("top_left.background"));

		BaseRenderer.drawRectangle(x, y + sY, z, (sX), 1, getStyle().asColor("bottom_right.background"));
		BaseRenderer.drawRectangle(x + (sX), y, z, 1, sY + 1, getStyle().asColor("bottom_right.background"));

		BaseRenderer.drawRectangle(x + 1, y + 1, z, ((sX) / l) * p - 1, sY - 1, getStyle().asColor("background.on"));
		BaseRenderer.drawRectangle(x + ((sX) / l) * p, y + 1, z, (sX) - ((sX) / l) * p, sY - 1, getStyle().asColor("background.off"));

		BaseRenderer.drawBeveledPanel(Math.min(x + sX - 7, x + (sX / l) * p), y - 1, z, 8, sY + 3, getStyle().asColor("top_left.foreground"), getStyle().asColor("foreground"), getStyle().asColor("bottom_right.foreground"));
	}
}
