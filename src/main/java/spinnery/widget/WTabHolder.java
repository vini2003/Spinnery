package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class WTabHolder extends WAbstractWidget implements WCollection, WDelegatedEventListener {
	List<WTab> tabs = new ArrayList<>();

	public void selectTab(int tabNumber) {
		for (int i = 0; i < tabs.size(); i++) {
			for (WAbstractWidget widget : tabs.get(i).getWidgets()) {
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
		WTab tab = new WTab(symbol, name, tabNumber);
		tabs.add(tab);
		int tabSize = getWidth() / tabs.size();
		int tabOffset = 0;
		for (int i = 0; i < tabs.size(); i++) {
			WTabToggle button = tabs.get(i).getToggle();
			button.setWidth(tabSize);
			button.setPosition(Position.of(this, tabOffset, 0, 0));
			tabOffset += tabSize;
		}
		return tab;
	}

	public WTab addTab(Item symbol, String name) {
		return addTab(symbol, new LiteralText(name));
	}

	public WTab removeTab(int tabNumber) {
		return tabs.remove(tabNumber);
	}

	@Override
	public void align() {
		super.align();

		for (WAbstractWidget widget : getWidgets()) {
			widget.align();
		}
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		Set<WAbstractWidget> widgets = new LinkedHashSet<>();
		for (WTab tab : tabs) {
			widgets.addAll(tab.getWidgets());
		}
		return widgets;
	}

	@Override
	public Set<WAbstractWidget> getAllWidgets() {
		Set<WAbstractWidget> widgets = new LinkedHashSet<>();
		for (WTab tab : tabs) {
			widgets.addAll(tab.getAllWidgets());
		}
		return widgets;
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return getWidgets();
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return getAllWidgets().containsAll(Arrays.asList(widgets));
	}

	@Override
	public void center() {
		super.center();

		for (WTab tab : tabs) {
			for (WAbstractWidget widget : tab.getWidgets()) {
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

		for (WAbstractWidget widget : getAllWidgets()) {
			if (!(widget instanceof WTabToggle)) {
				widget.draw();
			}
		}
	}

	public class WTab implements WModifiableCollection {
		protected Item symbol;
		protected Text name;
		protected int number;
		protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();

		public WTab(Item symbol, Text name, int number) {
			this.symbol = symbol;
			this.name = name;
			this.number = number;
			WTabToggle tabToggle = createChild(WTabToggle.class, Position.of(WTabHolder.this, 0, 0, 0), Size.of(36, 24))
					.setSymbol(symbol)
					.setLabel(name);
			tabToggle.setParent(WTabHolder.this);
			tabToggle.setInterface(WTabHolder.this.getInterface());
			this.widgets.add(tabToggle);
			this.widgets.iterator().next().setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> {
				if (getToggle().getToggleState()) {
					selectTab(this.number);
				}
			});
		}

		public Set<WAbstractWidget> getWidgets() {
			return widgets;
		}

		public WTabToggle getToggle() {
			return (WTabToggle) widgets.iterator().next();
		}

		@Override
		public void add(WAbstractWidget... widgets) {
			this.widgets.addAll(Arrays.asList(widgets));
			for (WAbstractWidget newWidget : widgets) {
				newWidget.setInterface(WTabHolder.this.getInterface());
				newWidget.setParent(WTabHolder.this);
			}
			for (WAbstractWidget widget : getWidgets()) {
				if (!(widget instanceof WTabToggle)) {
					widget.setHidden(true);
				}
			}
		}

		@Override
		public void remove(WAbstractWidget... widgets) {
			this.widgets.removeAll(Arrays.asList(widgets));
		}

		@Override
		public boolean contains(WAbstractWidget... widgets) {
			return this.widgets.containsAll(Arrays.asList(widgets));
		}
	}
}
