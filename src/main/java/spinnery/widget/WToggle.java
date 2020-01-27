package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class WToggle extends WWidget implements WClient, WFocusedMouseListener {
	public static final int TOP_LEFT_BACKGROUND = 0;
	public static final int BOTTOM_RIGHT_BACKGROUND = 1;
	public static final int BACKGROUND_ON = 2;
	public static final int BACKGROUND_OFF = 3;
	public static final int TOP_LEFT_FOREGROUND = 4;
	public static final int BOTTOM_RIGHT_FOREGROUND = 5;
	public static final int FOREGROUND = 6;
	public static final int LABEL = 7;
	protected boolean toggleState = false;

	public WToggle(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);

		setPosition(position);

		setSize(size);

		setTheme("light");
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.put(TOP_LEFT_BACKGROUND, WColor.of(rawTheme.get("top_left_background")));
		theme.put(BOTTOM_RIGHT_BACKGROUND, WColor.of(rawTheme.get("bottom_right_background")));
		theme.put(BACKGROUND_ON, WColor.of(rawTheme.get("background_on")));
		theme.put(BACKGROUND_OFF, WColor.of(rawTheme.get("background_off")));
		theme.put(TOP_LEFT_FOREGROUND, WColor.of(rawTheme.get("top_left_foreground")));
		theme.put(BOTTOM_RIGHT_FOREGROUND, WColor.of(rawTheme.get("bottom_right_foreground")));
		theme.put(FOREGROUND, WColor.of(rawTheme.get("foreground")));
		theme.put(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		setToggleState(!getToggleState());
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

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

		BaseRenderer.drawRectangle(x, y, z, sX, 1, getResourceAsColor(TOP_LEFT_BACKGROUND));
		BaseRenderer.drawRectangle(x, y, z, 1, sY, getResourceAsColor(TOP_LEFT_BACKGROUND));

		BaseRenderer.drawRectangle(x, y + sY, z, sX, 1, getResourceAsColor(BOTTOM_RIGHT_BACKGROUND));
		BaseRenderer.drawRectangle(x + sX, y, z, 1, sY + 1, getResourceAsColor(BOTTOM_RIGHT_BACKGROUND));

		BaseRenderer.drawRectangle(x + 1, y + 1, z, sX - 1, sY - 1, getToggleState() ? getResourceAsColor(BACKGROUND_ON) : getResourceAsColor(BACKGROUND_OFF));

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(x + sX - 8, y - 1, z, 8, sY + 3, getResourceAsColor(TOP_LEFT_FOREGROUND), getResourceAsColor(FOREGROUND), getResourceAsColor(BOTTOM_RIGHT_FOREGROUND));
		} else {
			BaseRenderer.drawBeveledPanel(x + 1, y - 1, z, 8, sY + 3, getResourceAsColor(TOP_LEFT_FOREGROUND), getResourceAsColor(FOREGROUND), getResourceAsColor(BOTTOM_RIGHT_FOREGROUND));
		}

		if (hasLabel()) {
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX + 2, (int) (y + sY / 2 - 4.5), getResourceAsColor(LABEL).RGB);
		}
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public void setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
	}
}
