package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

import java.util.Map;

public class WButton extends WWidget implements WClient {
	protected boolean toggleState = false;
	protected int toggleTicks = 0;
	protected WButton.Theme drawTheme;

	public WButton(WAnchor anchor, int positionX, int positionY, int positionZ, int sizeX, int sizeY, WInterface linkedPanel) {
		setInterface(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (scanFocus(mouseX, mouseY)) {
			setToggleState(!getToggleState());
			setToggleTicks(1);
		}

		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
			//
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getColor(TOP_LEFT_ON), getColor(BACKGROUND_ON), getColor(BOTTOM_RIGHT_ON));
		} else {
			BaseRenderer.drawBeveledPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(),getColor(TOP_LEFT_OFF), getColor(BACKGROUND_OFF), getColor(BOTTOM_RIGHT_OFF));
		}
		if (hasLabel()) {
			if (BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) > getSizeX() - 6) {
				BaseRenderer.getTextRenderer().drawWithShadow(getLabel().asFormattedString(), (float) (getPositionX() + getSizeX() + 2), (float) (getPositionY() + getSizeY() / 2 - 4.5), getColor(LABEL).RGB);
			} else {
				BaseRenderer.getTextRenderer().drawWithShadow(getLabel().asFormattedString(), (float) (getPositionX() + 3), (float) (getPositionY() + getSizeY() / 2 - 4.5), getColor(LABEL).RGB);
			}
		}
	}

	@Override
	public void tick() {
		if (getToggleTicks() > 0) {
			decrementToggleTicks(1);
		} else if (getToggleState()) {
			setToggleState(!getToggleState());
		}
	}

	public boolean getToggleState() {
		return toggleState;
	}

	public void setToggleState(boolean toggleState) {
		this.toggleState = toggleState;
	}

	public int getToggleTicks() {
		return toggleTicks;
	}

	public void setToggleTicks(int toggleTicks) {
		this.toggleTicks = toggleTicks;
	}

	public void incrementToggleTicks(int increment) {
		toggleTicks += increment;
	}

	public void decrementToggleTicks(int decrement) {
		toggleTicks -= decrement;
	}

	public static final int TOP_LEFT_ON = 0;
	public static final int BOTTOM_RIGHT_ON = 1;
	public static final int BACKGROUND_ON = 2;
	public static final int TOP_LEFT_OFF = 3;
	public static final int BOTTOM_RIGHT_OFF = 4;
	public static final int BACKGROUND_OFF = 5;
	public static final int LABEL = 6;

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WInterface.Theme theme = new WWidget.Theme();
		theme.add(TOP_LEFT_ON, WColor.of(rawTheme.get("top_left_on")));
		theme.add(BOTTOM_RIGHT_ON, WColor.of(rawTheme.get("bottom_right_on")));
		theme.add(BACKGROUND_ON, WColor.of(rawTheme.get("background_on")));
		theme.add(TOP_LEFT_OFF, WColor.of(rawTheme.get("top_left_off")));
		theme.add(BOTTOM_RIGHT_OFF, WColor.of(rawTheme.get("bottom_right_off")));
		theme.add(BACKGROUND_OFF, WColor.of(rawTheme.get("background_off")));
		theme.add(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}
}
