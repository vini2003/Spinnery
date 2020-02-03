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
public class WDraggableArea extends WWidget implements WModifiableCollection {
	public static final int LABEL_HEIGHT = 16; // add to theme config?

	protected WDraggableContainer container;

	public WDraggableArea() {
		container = getFactory().build(WDraggableContainer.class, WPosition.of(this, 4, 4, 0), WSize.of(getWidth() - 8, getHeight() - 8));
		container.setParent(this);
	}

	@Override
	public void onLayoutChange() {
		if (hasLabel()) {
			container.setPosition(WPosition.of(this, 6, 16 + 2 + 3, container.getPosition().getOffsetZ()));
			container.setSize(WSize.of(getWidth() - 8, getHeight() - (16 + 2 + 3) - 6));
		} else {
			container.setPosition(WPosition.of(this, 4, 4, 0));
			container.setSize(WSize.of(getWidth() - 8, getHeight() - 8));
		}
		container.updateHidden();
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
	public boolean updateFocus(int mouseX, int mouseY) {
		setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WWidget::getFocus)));
		return getFocus();
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

		BaseRenderer.drawGradient(container.getX(), container.getY(),
				container.getX() + container.getWidth(), container.getY() + container.getHeight(),
				z, getStyle().asColor("area.start"), getStyle().asColor("area.end"));

		container.draw();
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
	public boolean contains(WWidget... widgetArray) {
		return container.contains(widgetArray);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		container.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		container.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		container.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	@Override
	public void tick() {
		container.tick();
	}
}
