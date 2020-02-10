package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WCollection;
import spinnery.widget.api.WDelegatedEventListener;
import spinnery.widget.api.WDrawableCollection;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class WTabHolder extends WAbstractWidget implements WCollection, WDelegatedEventListener {
	List<WTab> tabs = new ArrayList<>();

	public void selectTab(int tabNumber) {
		for (WTab tab : tabs) {
			if (tab.getNumber() != tabNumber) {
				tab.getToggle().setToggleState(false);
			}
			for (WAbstractWidget widget : tab.getWidgets()) {
				widget.setHidden(tab.getNumber() != tabNumber);
			}
		}
	}

	public WTab addTab(Item symbol, String name) {
		return addTab(symbol, new LiteralText(name));
	}

	public WTab addTab(Item symbol, Text name) {
		int tabNumber = tabs.size() + 1;
		WTab tab = new WTab()
				.setSymbol(symbol).setName(name).setNumber(tabNumber)
				.setParent(this).setInterface(getInterface());
		tabs.add(tab);
		int tabSize = getWidth() / tabs.size();
		int tabOffset = 0;
		for (WTab oldTab : tabs) {
			oldTab.setWidth(tabSize);
			oldTab.setPosition(Position.of(this, tabOffset, 0, 0));
			WTabToggle toggle = oldTab.getToggle();
			toggle.setWidth(tabSize);
			toggle.setPosition(Position.of(this, tabOffset, 0, 0));
			tabOffset += tabSize;
		}
		return tab;
	}

	public WTab removeTab(int tabNumber) {
		return tabs.remove(tabNumber);
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return new HashSet<>(tabs);
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
	public boolean contains(WAbstractWidget... widgets) {
		return getAllWidgets().containsAll(Arrays.asList(widgets));
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return tabs;
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

		for (WTab tab : tabs) {
			tab.draw();
		}
	}

	public class WTab extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WDelegatedEventListener {
		protected int number;
		protected WTabToggle toggle;
		protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();
		protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

		public WTab() {
			toggle = WWidgetFactory.buildDetached(WTabToggle.class)
					.setPosition(Position.of(WTabHolder.this, 0, 0, 0))
					.setSize(Size.of(36, 24))
					.setParent(this)
					.setInterface(WTabHolder.this.getInterface());
		}

		@Override
		public Collection<? extends WEventListener> getEventDelegates() {
			Set<WAbstractWidget> delegates = new HashSet<>(widgets);
			delegates.add(toggle);
			return delegates;
		}

		@Override
		public void add(WAbstractWidget... widgets) {
			for (WAbstractWidget newWidget : widgets) {
				newWidget.setInterface(WTabHolder.this.getInterface());
				newWidget.setParent(this);
				this.widgets.add(newWidget);
			}
			for (WAbstractWidget widget : getWidgets()) {
				if (!(widget instanceof WTabToggle)) {
					widget.setHidden(true);
				}
			}
			onLayoutChange();
		}

		public Set<WAbstractWidget> getWidgets() {
			return widgets;
		}

		@Override
		public void onLayoutChange() {
			recalculateCache();
		}

		@Override
		public void recalculateCache() {
			orderedWidgets = new ArrayList<>(getWidgets());
			Collections.sort(orderedWidgets);
			Collections.reverse(orderedWidgets);
		}

		@Override
		public List<WLayoutElement> getOrderedWidgets() {
			return orderedWidgets;
		}

		@Override
		public boolean contains(WAbstractWidget... widgets) {
			return this.widgets.containsAll(Arrays.asList(widgets));
		}

		@Override
		public void remove(WAbstractWidget... widgets) {
			this.widgets.removeAll(Arrays.asList(widgets));
			onLayoutChange();
		}

		@Override
		public void draw() {
			for (WLayoutElement widget : getOrderedWidgets()) {
				widget.draw();
			}
		}

		public int getNumber() {
			return number;
		}

		public WTab setNumber(int number) {
			this.number = number;
			return this;
		}

		public WTab setSymbol(ItemConvertible symbol) {
			getToggle().setSymbol(symbol);
			return this;
		}

		public WTabToggle getToggle() {
			return toggle;
		}

		public WTab setName(Text name) {
			getToggle().setLabel(name);
			return this;
		}
	}
}
