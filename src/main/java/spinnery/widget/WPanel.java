package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class WPanel extends WAbstractWidget implements WModifiableCollection, WDrawableCollection, WDelegatedEventListener {
	protected Set<WAbstractWidget> heldWidgets = new LinkedHashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	@Override
	public void draw() {
		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawPanel(x, y, z, sX, sY, getStyle().asColor("shadow"), getStyle().asColor("background"), getStyle().asColor("highlight"), getStyle().asColor("outline"));

		if (hasLabel()) {
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + 8, y + 6, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
		}

		for (WLayoutElement widget : getOrderedWidgets()) {
			widget.draw();
		}
	}

	@Override
	public void add(WAbstractWidget... widgets) {
		heldWidgets.addAll(Arrays.asList(widgets));
		onLayoutChange();
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();
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
	public Set<WAbstractWidget> getWidgets() {
		return heldWidgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return heldWidgets.containsAll(Arrays.asList(widgets));
	}

	@Override
	public void remove(WAbstractWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
		onLayoutChange();
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return getWidgets();
	}
}
