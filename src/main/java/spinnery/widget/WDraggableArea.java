package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WDraggableArea extends WWidget implements WClient, WModifiableCollection {
	public static final int LABEL_HEIGHT = 16; // add to theme config?

	protected WDraggableContainer container;

	public WDraggableArea(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);

		container = new WDraggableContainer(WPosition.of(WType.ANCHORED, 4, 4, getZ(), this),
				WSize.of(getWidth() - 8, getHeight() - 8), linkedInterface);
		linkedInterface.add(container);
	}

	@Override
	public List<WWidget> getWidgets() {
		return container.getWidgets();
	}

	@Override
	public List<WWidget> getAllWidgets() {
		return container.getAllWidgets();
	}

	@Override
	public void setLabel(Text label) {
		super.setLabel(label);
		container.setPosition(WPosition.of(WType.ANCHORED, 6, 16 + 2 + 3, getZ(), this));
		container.setHeight(getHeight() - (16 + 2 + 3) - 6);
		container.updateHidden();
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
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6, getStyle().asColor("label"));
			BaseRenderer.drawRectangle(x, y + LABEL_HEIGHT, z, sX, 1, getStyle().asColor("outline"));
			BaseRenderer.drawRectangle(x + 1, y + LABEL_HEIGHT + 1, z, sX - 2, 0.75, getStyle().asColor("shadow"));
		}

		BaseRenderer.drawGradient(container.getX(), container.getY(),
				container.getX() + container.getWidth(), container.getY() + container.getHeight(),
				z, getStyle().asColor("area.start"), getStyle().asColor("area.end"));
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
}
