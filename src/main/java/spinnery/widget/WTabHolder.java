package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.WCollection;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WTabHolder extends WWidget implements WCollection {
	List<WTab> tabs = new ArrayList<>();

	public WTabHolder build() {
		return this;
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
			button.setPosition(new WPosition().anchor(this).position(tabOffset, 0, 0));
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
	public Set<WWidget> getWidgets() {
		Set<WWidget> widgets = new LinkedHashSet<>();
		for (WTab tab : tabs) {
			widgets.addAll(tab.getWidgets());
		}
		return widgets;
	}

	@Override
	public Set<WWidget> getAllWidgets() {
		Set<WWidget> widgets = new LinkedHashSet<>();
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

		BaseRenderer.drawPanel(x, y + 24, z, sX, sY - 24, getStyle().asColor("shadow"), getStyle().asColor("background"), getStyle().asColor("highlight"), getStyle().asColor("outline"));

		for (WWidget widget : getAllWidgets()) {
			if (!(widget instanceof WTabToggle)) {
				widget.draw();
			}
		}
	}

	public class WTab implements WModifiableCollection {
		Item symbol;
		Text name;
		int number;
		Set<WWidget> widgets = new LinkedHashSet<>();

		public WTab(WTabHolder holder, Item symbol, Text name, int number) {
			this.symbol = symbol;
			this.name = name;
			this.number = number;
			this.widgets.add(getInterface().getFactory().build(WTabToggle.class, new WPosition().anchor(holder).position(0, 0, 0), new WSize().put(36, 24))
						.symbol(symbol)
						.name(name)
						.build());
			this.widgets.iterator().next().setOnMouseClicked(() -> {
				if (getToggle().getToggleState()) {
					selectTab(this.number);
				}
			});
		}

		public Set<WWidget> getWidgets() {
			return widgets;
		}

		public WTabToggle getToggle() {
			return (WTabToggle) widgets.iterator().next();
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
