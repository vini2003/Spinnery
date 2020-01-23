package spinnery.widget;

import spinnery.client.BaseRenderer;

import java.util.Map;

public class WButton extends WWidget implements WClient {
	public static final int TOP_LEFT_ON = 0;
	public static final int BOTTOM_RIGHT_ON = 1;
	public static final int BACKGROUND_ON = 2;
	public static final int TOP_LEFT_OFF = 3;
	public static final int BOTTOM_RIGHT_OFF = 4;
	public static final int BACKGROUND_OFF = 5;
	public static final int LABEL = 6;
	protected boolean toggleState = false;
	protected int toggleTicks = 0;

	public WButton(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);

		setPosition(position);

		setSize(size);

		setTheme("light");
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(TOP_LEFT_ON, WColor.of(rawTheme.get("top_left_on")));
		theme.add(BOTTOM_RIGHT_ON, WColor.of(rawTheme.get("bottom_right_on")));
		theme.add(BACKGROUND_ON, WColor.of(rawTheme.get("background_on")));
		theme.add(TOP_LEFT_OFF, WColor.of(rawTheme.get("top_left_off")));
		theme.add(BOTTOM_RIGHT_OFF, WColor.of(rawTheme.get("bottom_right_off")));
		theme.add(BACKGROUND_OFF, WColor.of(rawTheme.get("background_off")));
		theme.add(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
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
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		if (getToggleState()) {
			BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getColor(TOP_LEFT_ON), getColor(BACKGROUND_ON), getColor(BOTTOM_RIGHT_ON));
		} else {
			BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getColor(TOP_LEFT_OFF), getColor(BACKGROUND_OFF), getColor(BOTTOM_RIGHT_OFF));
		}
		if (hasLabel()) {
			if (BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) > getWidth() - 6) {
				BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), (getX() + getWidth() + 2), (int) (getY() + getHeight() / 2 - 4.5), getColor(LABEL).RGB);
			} else {
				BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), (getX() + 3), (int) (getY() + getHeight() / 2 - 4.5), getColor(LABEL).RGB);
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
}
