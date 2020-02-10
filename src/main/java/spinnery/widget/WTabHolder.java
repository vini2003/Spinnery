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
	List<WTab> widgets = new ArrayList<>();

	public void selectTab(int tabNumber) {
		for (int i = 0; i < widgets.size(); i++) {
			for (WAbstractWidget widget : widgets.get(i).getWidgets()) {
				if (widget != widgets.get(i).getToggle()) {
					widget.setHidden(i + 1 != tabNumber);
				} else {
					if (i + 1 != tabNumber) {
						widgets.get(i).getToggle().setToggleState(false);
					}
				}
			}
		}
	}

	public WTab addTab(Item symbol, Text name) {
		int tabNumber = widgets.size() + 1;
		WTab tab = new WTab(symbol, name, tabNumber);
		widgets.add(tab);
		int tabSize = getWidth() / widgets.size();
		int tabOffset = 0;
		for (int i = 0; i < widgets.size(); i++) {
			WTab oldTab = widgets.get(i);
			oldTab.setWidth(tabSize);
			oldTab.setPosition(Position.of(this, tabOffset, 0, 0));
			WTabToggle toggle = widgets.get(i).getToggle();
			toggle.setWidth(tabSize);
			toggle.setPosition(Position.of(this, tabOffset, 0, 0));
			tabOffset += tabSize;
		}
		return tab;
	}

	public WTab addTab(Item symbol, String name) {
		return addTab(symbol, new LiteralText(name));
	}

	public WTab removeTab(int tabNumber) {
		return widgets.remove(tabNumber);
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
		return new HashSet<>(widgets);
	}

	@Override
	public Set<WAbstractWidget> getAllWidgets() {
		Set<WAbstractWidget> widgets = new LinkedHashSet<>();
		for (WTab tab : this.widgets) {
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

		for (WTab tab : widgets) {
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

		BaseRenderer.drawPanel(x, y + 24, z, sX, sY - 24, getStyle().asColor("shadow"), getStyle().asColor("background"), getStyle().asColor("highlight"), getStyle().asColor("outline"));

		for (WAbstractWidget widget : getWidgets()) {
			widget.draw();
		}
	}

	public class WTab extends WAbstractWidget implements WDrawableCollection, WModifiableCollection {
		protected Item symbol;
		protected Text name;
		protected int number;
		protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();
		protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

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
			this.setOnMouseClicked((widget, mouseX, mouseY, button) -> {
				tabToggle.onMouseClicked(mouseX, mouseY, button);
			});
		}

		public Set<WAbstractWidget> getWidgets() {
			return widgets;
		}

		public WTabToggle getToggle() {
			return (WTabToggle) widgets.iterator().next();
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
			onLayoutChange();
		}

		@Override
		public void remove(WAbstractWidget... widgets) {
			this.widgets.removeAll(Arrays.asList(widgets));
			onLayoutChange();
		}

		@Override
		public boolean contains(WAbstractWidget... widgets) {
			return this.widgets.containsAll(Arrays.asList(widgets));
		}

		@Override
		public void draw() {
			for (WLayoutElement widget : getOrderedWidgets()) {
				widget.draw();
			}
		}
	}
}
