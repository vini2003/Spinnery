package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.Text;

import spinnery.client.render.TextRenderer;
import spinnery.widget.api.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class WTabHolder extends WAbstractWidget implements WCollection, WDelegatedEventListener {
	public enum Mode {
		OCCUPY_ALL,
		OCCUPY_PARTIAL
	}

	protected List<WTab> tabs = new ArrayList<>();

	protected Mode mode = Mode.OCCUPY_ALL;

	public void selectTab(int tabNumber) {
		for (WTab tab : tabs) {
			tab.setActive(tab.getNumber() == tabNumber);
		}
	}

	public WTab addTab(Text name) {
		return addTab(null, name);
	}

	public WTab addTab(Item symbol) {
		return addTab(symbol, null);
	}

	public WTab addTab(Item symbol, Text name) {
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

	public <W extends WTabHolder> W setMode(Mode mode) {
		this.mode = mode;
		return (W) this;
	}

	public Mode getMode() {
		return mode;
	}

	protected void updateTabs() {
		if (tabs.size() == 0) return;
		float baseTabSize = getWidth() / tabs.size();
		float tabSize = baseTabSize;
		float tabOffset = 0;
		for (WTab tab : tabs) {
			if (mode == Mode.OCCUPY_PARTIAL) {
				tabSize = 0;
				tabSize += tab.getToggle().symbol != null ? 18 : 0;
				tabSize += tab.getToggle().label != null ? TextRenderer.width(tab.getToggle().label) : 0;

				if (tab.getToggle().getLabel() != null && tab.getToggle().getSymbol() != null) {
					tabSize += 11;
				} else if (tab.getToggle().getLabel() != null) {
					tabSize += 15;
				} else {
					tabSize += 6;
				}
			}
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
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		for (WTab tab : tabs) {
			tab.draw(matrices, provider);
		}

		for (WTab tab : tabs) {
			tab.getToggle().draw(matrices, provider);
		}
	}

	public class WTab extends WAbstractWidget implements WModifiableCollection, WDelegatedEventListener {
		protected int number;
		protected WTabToggle toggle;
		protected WPanel body = new WPanel();

		public WTab() {
			toggle = new WTabToggle()
					.setPosition(Position.of(WTabHolder.this, 0, 0, 0))
					.setSize(Size.of(36, 24))
					.setParent(this)
					.setInterface(WTabHolder.this.getInterface());

			body.setParent(this).getPosition().setAnchor(WTabHolder.this).setOffsetY(24);
		}

		@Override
		public Collection<? extends WEventListener> getEventDelegates() {
			Collection<? extends WEventListener> bodyDelegates = body.getEventDelegates();
			Collection<WEventListener> returnList = new ArrayList<>(bodyDelegates);
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
		}

		@Override
		public boolean contains(WAbstractWidget... widgets) { return body.contains(widgets); }

		@Override
		public void remove(WAbstractWidget... widgets) {
			body.remove(widgets);
			onLayoutChange();
		}

		@Override
		public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
			body.draw(matrices, provider);
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

		public void setActive(boolean isActive) {
			getToggle().setToggleState(isActive);
			body.setHidden(!isActive);
		}

		public WPanel getBody() { return body; }
	}
}
