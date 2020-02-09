package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WDelegatedEventListener;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WModifiableCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class WPanel extends WAbstractWidget implements WModifiableCollection, WDelegatedEventListener {
	protected Set<WAbstractWidget> heldWidgets = new LinkedHashSet<>();

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
					.text(getLabel()).at(x + sX / 2 - TextRenderer.width(getLabel()) / 2, y + 6, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
			BaseRenderer.drawRectangle(x, y + 16, z, sX, 1, getStyle().asColor("outline"));
			BaseRenderer.drawRectangle(x + 1, y + 17, z, sX - 2, 0.75, getStyle().asColor("shadow"));
		}

		for (WAbstractWidget widget : getWidgets()) {
			widget.draw();
		}
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
	public void add(WAbstractWidget... widgets) {
		heldWidgets.addAll(Arrays.asList(widgets));
	}

	@Override
	public void remove(WAbstractWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return getWidgets();
	}
}
