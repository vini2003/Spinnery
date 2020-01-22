package spinnery.widget;

import spinnery.client.BaseRenderer;

import java.util.Map;

public class WToggle extends WWidget implements WClient {
	public static final int TOP_LEFT_BACKGROUND = 0;
	public static final int BOTTOM_RIGHT_BACKGROUND = 1;
	public static final int BACKGROUND_ON = 2;
	public static final int BACKGROUND_OFF = 3;
	public static final int TOP_LEFT_FOREGROUND = 4;
	public static final int BOTTOM_RIGHT_FOREGROUND = 5;
	public static final int FOREGROUND = 6;
	public static final int LABEL = 7;
	protected boolean toggleState = false;

	public WToggle(WAnchor anchor, int positionX, int positionY, int positionZ, int sizeX, int sizeY, WInterface linkedPanel) {
		setInterface(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");
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
		theme.add(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (scanFocus(mouseX, mouseY)) {
			setToggleState(!getToggleState());
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getPositionX();
		int y = getPositionY();
		int z = getPositionZ();

		int sX = getSizeX();
		int sY = getSizeY();

		BaseRenderer.drawRectangle(x, y, z, sX, 1, getColor(TOP_LEFT_BACKGROUND));
		BaseRenderer.drawRectangle(x, y, z, 1, sY, getColor(TOP_LEFT_BACKGROUND));

		BaseRenderer.drawRectangle(x, y + sY, z, sX, 1, getColor(BOTTOM_RIGHT_BACKGROUND));
		BaseRenderer.drawRectangle(x + sX, y, z, 1, sY + 1, getColor(BOTTOM_RIGHT_BACKGROUND));

		BaseRenderer.drawRectangle(x + 1, y + 1, z, sX - 1, sY - 1, getToggleState() ? getColor(BACKGROUND_ON) : getColor(BACKGROUND_OFF));

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(x + sX - 8, y - 1, z, 8, sY + 3, getColor(TOP_LEFT_FOREGROUND), getColor(FOREGROUND), getColor(BOTTOM_RIGHT_FOREGROUND));
		} else {
			BaseRenderer.drawBeveledPanel(x + 1, y - 1, z, 8, sY + 3, getColor(TOP_LEFT_FOREGROUND), getColor(FOREGROUND), getColor(BOTTOM_RIGHT_FOREGROUND));
		}

		if (hasLabel()) {
			BaseRenderer.getTextRenderer().drawWithShadow(getLabel().asFormattedString(), (float) (positionX + sizeX + 2), (float) (positionY + sizeY / 2 - 4.5), getColor(LABEL).RGB);
		}
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public void setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
	}
}
