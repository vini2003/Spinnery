package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class WHorizontalList extends WWidget implements WModifiableCollection {
	public static final int LABEL_HEIGHT = 16; // add to theme config?

	protected WHorizontalScrollableContainer container;

	public WHorizontalList build() {
		container = getInterface().getFactory().build(WHorizontalScrollableContainer.class, new WPosition().anchor(this).position(6, 4, getZ()), new WSize().put(getWidth() - 12, getHeight() - 8))
				.build();
		return this;
	}

	@Override
	public Set<WWidget> getWidgets() {
		return container.getWidgets();
	}

	@Override
	public Set<WWidget> getAllWidgets() {
		return container.getAllWidgets();
	}

	@Override
	public boolean contains(WWidget... widgetArray) {
		return container.contains(widgetArray);
	}

	public boolean hasScrollbar() {
		return container.hasScrollbar();
	}

	public void setScroller(boolean visible) {
		container.setScrollbarVisible(visible);
	}

	@Override
	public void align() {
		super.align();
		container.scrollToStart();
	}

	@Override
	public boolean updateFocus(int mouseX, int mouseY) {
		setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WWidget::getFocus)));
		return getFocus();
	}

	@Override
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		container.onMouseScrolled(mouseX, mouseY, deltaY);
	}

	@Override
	public void add(WWidget... widgetArray) {
		container.add(widgetArray);
	}

	@Override
	public void remove(WWidget... widgetArray) {
		container.remove(widgetArray);
	}

	@Override
	public void setLabel(Text label) {
		super.setLabel(label);
		container.setPosition(new WPosition().anchor(this).position(6, 16 + 2 + 3, container.getZ()));
		container.setHeight(getHeight() - (16 + 2 + 3) - 6);
		container.scrollToStart();
		container.updateHidden();
		container.updateScrollbar();
	}

	@Override
	public void tick() {
		container.tick();
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

		BaseRenderer.drawPanel(x, y, z, sX, sY, getStyle().asColor("shadow"), getStyle().asColor("background"), getStyle().asColor("highlight"), getStyle().asColor("outline"));

		if (hasLabel()) {
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + sX / 2 - TextRenderer.width(getLabel()) / 2, y + 6, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
			BaseRenderer.drawRectangle(x, y + LABEL_HEIGHT, z, sX, 1, getStyle().asColor("outline"));
			BaseRenderer.drawRectangle(x + 1, y + LABEL_HEIGHT + 1, z, sX - 2, 0.75, getStyle().asColor("shadow"));
		}

		container.draw();
	}
}
