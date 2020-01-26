package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class WTabHolder extends WWidget implements WClient, WCollection, WFocusedMouseListener {
	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	List<WTab> tabs = new ArrayList<>();

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
		for (int i = 0; i < tabs.size(); i++) {
			for (WWidget widget : tabs.get(i).getWidgets()) {
				if (widget != tabs.get(i).getToggle()) {
					widget.setHidden(i + 1 != tabNumber);
				} else {
					if (i + 1 != tabNumber) {
						tabs.get(i).getToggle().setToggleState(false);
					}
				}
			}
		}
	}

	public WTab addTab(Item symbol, Text name) {
		int tabNumber = tabs.size() + 1;
		WTab tab = new WTab(this, symbol, name, tabNumber);
		tabs.add(tab);
		int tabSize = getWidth() / tabs.size();
		int tabOffset = 0;
		for (int i = 0; i < tabs.size(); i++) {
			WTabToggle button = tabs.get(i).getToggle();
			button.setWidth(tabSize);
			button.setPosition(WPosition.of(WType.ANCHORED, tabOffset, 0, 0, this));
			tabOffset += tabSize;
		}
		return tab;
	}

	public WTab removeTab(int tabNumber) {
		return tabs.remove(tabNumber);
	}

	@Override
	public void align() {
		super.align();

		for (WWidget widget : getWidgets()) {
			widget.align();
		}
	}

	@Override
	public List<WWidget> getWidgets() {
		List<WWidget> widgets = new LinkedList<>();
		for (WTab tab : tabs) {
			widgets.addAll(tab.getWidgets());
		}
		return widgets;
	}

	@Override
	public List<WWidget> getAllWidgets() {
		List<WWidget> widgets = new LinkedList<>();
		for (WTab tab : tabs) {
			widgets.addAll(tab.getAllWidgets());
		}
		return widgets;
	}

	@Override
	public boolean contains(WWidget... widgets) {
		return getAllWidgets().containsAll(Arrays.asList(widgets));
	}

	@Override
	public void center() {
		super.center();

		for (WTab tab : tabs) {
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

		for (WTab tab : tabs) {
			tab.getToggle().draw();
		}

		BaseRenderer.drawPanel(x, y + 24, z, sX, sY - 24, getColor(SHADOW), getColor(BACKGROUND), getColor(HIGHLIGHT), getColor(OUTLINE));

		for (WWidget widget : getWidgets()) {
			if (!(widget instanceof WTabToggle)) {
				widget.draw();
			}
		}
	}

	public class WTab implements WModifiableCollection {
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
				if (getToggle().getToggleState()) {
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

		@Override
		public void add(WWidget... widgets) {
			this.widgets.addAll(Arrays.asList(widgets));
			for (WWidget widget : getWidgets()) {
				if (!(widget instanceof WTabToggle)) {
					widget.setHidden(true);
				}
			}
		}

		@Override
		public void remove(WWidget... widgets) {
			this.widgets.removeAll(Arrays.asList(widgets));
		}

		@Override
		public boolean contains(WWidget... widgets) {
			return this.widgets.containsAll(Arrays.asList(widgets));
		}
	}
}
