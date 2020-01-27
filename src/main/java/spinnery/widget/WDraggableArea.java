package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class WDraggableArea extends WWidget implements WClient, WModifiableCollection {
	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	public static final int LABEL = 4;
	public static final int AREA_START = 5;
	public static final int AREA_END = 6;
	public static final int LABEL_HEIGHT = 16; // add to theme config?

	protected WDraggableContainer container;

	public WDraggableArea(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setTheme("light");

		container = new WDraggableContainer(WPosition.of(WType.ANCHORED, 4, 4, getZ(), this),
				WSize.of(getWidth() - 8, getHeight() - 8), linkedInterface);
		linkedInterface.add(container);
	}

	public static Theme of(Map<String, String> rawTheme) {
		Theme theme = new Theme();
		theme.put(SHADOW, WColor.of(rawTheme.get("shadow")));
		theme.put(BACKGROUND, WColor.of(rawTheme.get("background")));
		theme.put(HIGHLIGHT, WColor.of(rawTheme.get("highlight")));
		theme.put(OUTLINE, WColor.of(rawTheme.get("outline")));
		theme.put(LABEL, WColor.of(rawTheme.get("label")));
		theme.put(AREA_START, WColor.of(rawTheme.get("area_start")));
		theme.put(AREA_END, WColor.of(rawTheme.get("area_end")));
		return theme;
	}

	@Override
	public List<WWidget> getWidgets() {
		return container.getWidgets();
	}

	@Override
	public List<WWidget> getAllWidgets() {
		return container.getAllWidgets();
	}

	@Override
	public void setLabel(Text label) {
		super.setLabel(label);
		container.setPosition(WPosition.of(WType.ANCHORED, 6, 16 + 2 + 3, getZ(), this));
		container.setHeight(getHeight() - (16 + 2 + 3) - 6);
		container.updateHidden();
	}

	@Override
	public boolean updateFocus(int mouseX, int mouseY) {
		setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WWidget::getFocus)));
		return getFocus();
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

		BaseRenderer.drawPanel(x, y, z, sX, sY, getResourceAsColor(SHADOW), getResourceAsColor(BACKGROUND), getResourceAsColor(HIGHLIGHT), getResourceAsColor(OUTLINE));

		if (hasLabel()) {
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6, getResourceAsColor(LABEL).RGB);
			BaseRenderer.drawRectangle(x, y + LABEL_HEIGHT, z, sX, 1, getResourceAsColor(OUTLINE));
			BaseRenderer.drawRectangle(x + 1, y + LABEL_HEIGHT + 1, z, sX - 2, 0.75, getResourceAsColor(SHADOW));
		}

		BaseRenderer.drawGradient(container.getX(), container.getY(),
				container.getX() + container.getWidth(), container.getY() + container.getHeight(),
				z, getResourceAsColor(AREA_START), getResourceAsColor(AREA_END));
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	@Override
	public void add(WWidget... widgetArray) {
		container.add(widgetArray);
	}

	@Override
	public void remove(WWidget... widgetArray) {
		container.remove(widgetArray);
	}

	@Override
	public boolean contains(WWidget... widgetArray) {
		return container.contains(widgetArray);
	}
}
