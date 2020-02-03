package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;
import spinnery.widget.api.WType;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WPanel extends WWidget implements WModifiableCollection {
	protected Set<WWidget> heldWidgets = new LinkedHashSet<>();

	public WPanel(WPosition position, WSize size, WInterface linkedInterface) {
		setPosition(position);
		setSize(size);
		setInterface(linkedInterface);
	}

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

		for (WWidget widget : getWidgets()) {
			widget.draw();
		}
	}

	@Override
	public Set<WWidget> getWidgets() {
		return heldWidgets;
	}

	@Override
	public boolean contains(WWidget... widgets) {
		return heldWidgets.containsAll(Arrays.asList(widgets));
	}

	@Override
	public void add(WWidget... widgets) {
		heldWidgets.addAll(Arrays.asList(widgets));
	}

	@Override
	public void remove(WWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
	}
}
