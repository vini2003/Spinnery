package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class WButton extends WWidget implements WClient, WFocusedMouseListener {
	public static final int TOP_LEFT_ON = 0;
	public static final int BOTTOM_RIGHT_ON = 1;
	public static final int BACKGROUND_ON = 2;
	public static final int TOP_LEFT_OFF = 3;
	public static final int BOTTOM_RIGHT_OFF = 4;
	public static final int BACKGROUND_OFF = 5;
	public static final int LABEL = 6;
	protected boolean lowered = false;
	protected int ticks = 0;

	public WButton(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setTheme("light");
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.put(TOP_LEFT_ON, WColor.of(rawTheme.get("top_left_on")));
		theme.put(BOTTOM_RIGHT_ON, WColor.of(rawTheme.get("bottom_right_on")));
		theme.put(BACKGROUND_ON, WColor.of(rawTheme.get("background_on")));
		theme.put(TOP_LEFT_OFF, WColor.of(rawTheme.get("top_left_off")));
		theme.put(BOTTOM_RIGHT_OFF, WColor.of(rawTheme.get("bottom_right_off")));
		theme.put(BACKGROUND_OFF, WColor.of(rawTheme.get("background_off")));
		theme.put(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		setLowered(true);
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

		if (isLowered()) {
			BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getResourceAsColor(TOP_LEFT_ON), getResourceAsColor(BACKGROUND_ON), getResourceAsColor(BOTTOM_RIGHT_ON));
		} else {
			BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getResourceAsColor(TOP_LEFT_OFF), getResourceAsColor(BACKGROUND_OFF), getResourceAsColor(BOTTOM_RIGHT_OFF));
		}

		if (hasLabel()) {
			if (BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) > getWidth() - 6) {
				BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), (getX() + getWidth() + 2), (int) (getY() + getHeight() / 2 - 4.5), getResourceAsColor(LABEL).RGB);
			} else {
				BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), (getX() + 3), (int) (getY() + getHeight() / 2 - 4.5), getResourceAsColor(LABEL).RGB);
			}
		}
	}

	@Override
	public void tick() {
		if (isLowered() && ticks == 0) {
			setLowered(false);
		} else if (isLowered() && ticks > 0) {
			--ticks;
		}
	}

	public boolean isLowered() {
		return lowered;
	}

	public void setLowered(boolean toggleState) {
		this.lowered = toggleState;
		this.ticks = toggleState ? 2 : 0;
	}
}
