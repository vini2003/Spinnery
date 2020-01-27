package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class WVerticalList extends WWidget implements WClient, WModifiableCollection {
	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	public static final int LABEL = 4;
	public static final int LABEL_HEIGHT = 16; // add to theme config?

	protected WVerticalScrollableContainer container;

	public WVerticalList(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setTheme("light");

		container = new WVerticalScrollableContainer(WPosition.of(WType.ANCHORED, 4, 6, getZ(), this),
				WSize.of(getWidth() - 8, getHeight() - 12), linkedInterface);
		linkedInterface.add(container);
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(SHADOW, WColor.of(rawTheme.get("shadow")));
		theme.add(BACKGROUND, WColor.of(rawTheme.get("background")));
		theme.add(HIGHLIGHT, WColor.of(rawTheme.get("highlight")));
		theme.add(OUTLINE, WColor.of(rawTheme.get("outline")));
		theme.add(LABEL, WColor.of(rawTheme.get("label")));
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
		container.setPosition(WPosition.of(WType.ANCHORED, 4, 16 + 2 + 3, getZ(), this));
		container.setHeight(getHeight() - (16 + 2 + 3) - 6);
		container.scrollToStart();
		container.updateHidden();
		container.updateScrollbar();
	}

	public boolean hasScrollbar() {
		return container.hasScrollbar();
	}

	public void setScroller(boolean visible) {
		container.setScrollbarVisible(visible);
	}

	@Override
	public void align() {
		super.align();
		container.scrollToStart();
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

		BaseRenderer.drawPanel(x, y, z, sX, sY, getColor(SHADOW), getColor(BACKGROUND), getColor(HIGHLIGHT), getColor(OUTLINE));

		if (hasLabel()) {
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6, getColor(LABEL).RGB);
			BaseRenderer.drawRectangle(x, y + LABEL_HEIGHT, z, sX, 1, getColor(OUTLINE));
			BaseRenderer.drawRectangle(x + 1, y + LABEL_HEIGHT + 1, z, sX - 2, 0.75, getColor(SHADOW));
		}
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
