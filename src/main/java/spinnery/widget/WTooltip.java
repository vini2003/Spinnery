package spinnery.widget;

import spinnery.client.BaseRenderer;
import spinnery.widget.api.Color;
import spinnery.widget.api.WDrawableCollection;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;

import java.util.*;

public class WTooltip extends WAbstractWidget implements WDrawableCollection, WModifiableCollection {
	protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	@Override
	public void draw() {
		if (isHidden()) return;

		int x = position.getX();
		int y = position.getY();
		int z = position.getRelativeZ();
		int width = size.getWidth();
		int height = size.getHeight();

		Color backgroundStart = getStyle().asColor("background.start");
		Color backgroundEnd = getStyle().asColor("background.end");
		Color colorStart = getStyle().asColor("outline.start");
		Color colorEnd = getStyle().asColor("outline.end");
		Color shadowStart = getStyle().asColor("shadow.start");
		Color shadowEnd = getStyle().asColor("shadow.end");

		// Vanilla drawing process
		BaseRenderer.drawGradient(x - 3, y - 4, x + width + 3, y - 3, z, shadowStart, shadowStart); // top border
		BaseRenderer.drawGradient(x - 3, y + height + 3, x + width + 3, y + height + 4, z, shadowEnd, shadowEnd); // bottom border
		BaseRenderer.drawGradient(x - 3, y - 3, x + width + 3, y + height + 3, z, backgroundStart, backgroundEnd); // body
		BaseRenderer.drawGradient(x - 4, y - 3, x - 3, y + height + 3, z, shadowStart, shadowEnd); // left border
		BaseRenderer.drawGradient(x + width + 3, y - 3, x + width + 4, y + height + 3, z, shadowStart, shadowEnd); // right border

		BaseRenderer.drawGradient(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, z, colorStart, colorEnd); // left outline
		BaseRenderer.drawGradient(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, z, colorStart, colorEnd); // right outline
		BaseRenderer.drawGradient(x - 3, y - 3, x + width + 3, y - 3 + 1, z, colorStart, colorStart); // top outline
		BaseRenderer.drawGradient(x - 3, y + height + 2, x + width + 3, y + height + 3, z, colorEnd, colorEnd); // bottom outline

		for (WLayoutElement widget : getOrderedWidgets()) {
			widget.draw();
		}
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return this.widgets.containsAll(Arrays.asList(widgets));
	}

	@Override
	public void add(WAbstractWidget... widgets) {
		this.widgets.addAll(Arrays.asList(widgets));
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
	public void remove(WAbstractWidget... widgets) {
		this.widgets.removeAll(Arrays.asList(widgets));
		onLayoutChange();
	}
}
