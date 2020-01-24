package spinnery.widget;

import net.minecraft.item.Item;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WTabHolder extends WWidget implements WClient, WCollection {
	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	Map<Integer, WTab> tabs = new HashMap<>();

	public WTabHolder(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);


		setPosition(position);

		setSize(size);

		setTheme("light");
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(SHADOW, WColor.of(rawTheme.get("shadow")));
		theme.add(BACKGROUND, WColor.of(rawTheme.get("background")));
		theme.add(HIGHLIGHT, WColor.of(rawTheme.get("highlight")));
		theme.add(OUTLINE, WColor.of(rawTheme.get("outline")));
		return theme;
	}

	public void selectTab(int tabNumber) {
		for (int i : tabs.keySet()) {
			for (WWidget widget : tabs.get(i).getWidgets()) {
				if (widget != tabs.get(i).getToggle()) {
					widget.setHidden(i != tabNumber);
				} else {
					if (i != tabNumber) {
						tabs.get(i).getToggle().setToggleState(false);
					}
				}
			}
		}
	}

	public WTab addTab(Item symbol, Text name) {
		int tabNumber = tabs.size() == 0 ? 1 : tabs.size() + 1;
		tabs.put(tabNumber, new WTab(this, symbol, name, tabNumber));
		int tabSize = getWidth() / tabs.size();
		int tabOffset = 0;
		for (int i : tabs.keySet()) {
			WTabToggle button = tabs.get(i).getToggle();
			button.setWidth(tabSize);
			button.setPosition(WPosition.of(WType.ANCHORED, tabOffset, 0, 0, this));
			tabOffset += tabSize;
		}
		return tabs.get(tabNumber);
	}

	@Override
	public List<WWidget> getWidgets() {
		List<WWidget> widgets = new LinkedList<>();
		for (int i : tabs.keySet()) {
			widgets.addAll(tabs.get(i).getWidgets());
		}
		return widgets;
	}

	@Override
	public void align() {
		super.align();

		for (WWidget widget : getWidgets()) {
			widget.align();
		}
	}

	@Override
	public void center() {
		super.center();

		for (int i : tabs.keySet()) {
			WTab tab = tabs.get(i);

			for (WWidget widget : tab.getWidgets()) {
				widget.align();
			}
		}
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

		for (int i : tabs.keySet()) {
			tabs.get(i).getToggle().draw();
		}

		BaseRenderer.drawPanel(x, y + 24, z, sX, sY - 24, getColor(SHADOW), getColor(BACKGROUND), getColor(HIGHLIGHT), getColor(OUTLINE));

		for (WWidget widget : getWidgets()) {
			if (!(widget instanceof WTabToggle)) {
				widget.draw();
			}
		}
	}

	public class WTab {
		Item symbol;
		Text name;
		int number;
		List<WWidget> widgets = new LinkedList<>();

		public WTab(WTabHolder holder, Item symbol, Text name, int number) {
			this.symbol = symbol;
			this.name = name;
			this.number = number;
			this.widgets.add(new WTabToggle(WPosition.of(WType.ANCHORED, 0, 0, 0, holder), WSize.of(36, 24), getInterface(), symbol, name));
			this.widgets.get(0).setOnMouseClicked(() -> {
				if (getToggle().getFocus() && getToggle().getToggleState()) {
					selectTab(this.number);
				}
			});
		}

		public List<WWidget> getWidgets() {
			return widgets;
		}

		public WTabToggle getToggle() {
			return (WTabToggle) widgets.get(0);
		}

		public void add(WWidget... widgets) {
			this.widgets.addAll(Arrays.asList(widgets));
			for (WWidget widget : getWidgets()) {
				if (!(widget instanceof WTabToggle)) {
					widget.setHidden(true);
				}
			}
		}

		public void remove(WWidget... widgets) {
			this.widgets.removeAll(Arrays.asList(widgets));
		}
	}
}
