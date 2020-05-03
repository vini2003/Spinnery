package com.github.vini2003.spinnery.widget;

import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.item.Item;
import com.github.vini2003.spinnery.widget.api.*;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class WTabHolder extends WAbstractWidget implements WCollection, WDelegatedEventListener {
	protected List<WTab> tabs = new ArrayList<>();

	public void selectTab(int tabNumber) {
		for (WTab tab : tabs) {
			tab.setActive(tab.getNumber() == tabNumber);
		}
	}

	public WTab addTab(Item symbol, String name) {
		return addTab(symbol, new StringTextComponent(name));
	}

	public WTab addTab(Item symbol, ITextComponent name) {
		int tabNumber = tabs.size() + 1;
		WTab tab = new WTab()
				.setSymbol(symbol).setName(name).setNumber(tabNumber)
				.setParent(this).setInterface(getInterface());
		tab.setActive(tabNumber == 1);
		tabs.add(tab);
		updateTabs();
		return tab;
	}

	public WTab removeTab(int tabNumber) {
		return tabs.remove(tabNumber);
	}

	protected void updateTabs() {
		if (tabs.size() == 0) return;
		int tabSize = getWidth() / tabs.size();
		int tabOffset = 0;
		for (WTab tab : tabs) {
			tab.setWidth(tabSize);
			tab.setPosition(Position.of(this, tabOffset, 0, 0));
			WTabToggle toggle = tab.getToggle();
			toggle.setWidth(tabSize);
			toggle.setPosition(Position.of(this, tabOffset, 0, 0));
			tabOffset += tabSize;
		}
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();
		updateTabs();
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

		for (WTab tab : tabs) {
			tab.getToggle().draw();
		}

		for (WTab tab : tabs) {
			tab.draw();
		}
	}

	public class WTab extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WDelegatedEventListener {
		protected int number;
		protected WTabToggle toggle;
		protected WPanel body = new WPanel();

		public WTab() {
			toggle = WWidgetFactory.buildDetached(WTabToggle.class)
					.setPosition(Position.of(WTabHolder.this, 0, 0, 0))
					.setSize(Size.of(36, 24))
					.setParent(this)
					.setInterface(WTabHolder.this.getInterface());

			body.setParent(this).getPosition().setAnchor(WTabHolder.this).setOffsetY(24);
		}

		@Override
		public Collection<? extends WEventListener> getEventDelegates() {
			Collection<? extends WEventListener> bodyDelegates = body.getEventDelegates();
			Collection<WEventListener> returnList = new ArrayList<WEventListener>(bodyDelegates);
			returnList.add(toggle);
			return returnList;
		}

		@Override
		public void add(WAbstractWidget... widgets) {
			body.add(widgets);
			onLayoutChange();
		}

		public Set<WAbstractWidget> getWidgets() { return body.getWidgets(); }

		@Override
		public void onLayoutChange() {
			super.onLayoutChange();
			body.setSize(WTabHolder.this.getSize().add(0, -24));
			recalculateCache();
		}

		@Override
		public void recalculateCache() {
			body.recalculateCache();
		}

		@Override
		public List<WLayoutElement> getOrderedWidgets() { return body.getOrderedWidgets(); }

		@Override
		public boolean contains(WAbstractWidget... widgets) { return body.contains(widgets); }

		@Override
		public void remove(WAbstractWidget... widgets) {
			body.remove(widgets);
			onLayoutChange();
		}

		@Override
		public void draw() {
			body.draw();
		}

		public int getNumber() {
			return number;
		}

		public WTab setNumber(int number) {
			this.number = number;
			return this;
		}

		public WTab setSymbol(IItemProvider symbol) {
			getToggle().setSymbol(symbol);
			return this;
		}

		public WTabToggle getToggle() {
			return toggle;
		}

		public WTab setName(ITextComponent name) {
			getToggle().setLabel(name);
			return this;
		}

		public void setActive(boolean isActive) {
			getToggle().setToggleState(isActive);
			body.setHidden(!isActive);
		}

		public WPanel getBody() { return body; }
	}
}
