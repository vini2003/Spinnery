package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WDropdown extends WWidget implements WClient, WModifiableCollection, WFocusedMouseListener {
	public List<List<WWidget>> dropdownWidgets = new ArrayList<>();
	protected boolean state = false;

	public WDropdown(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		updateHidden();
	}

	public void updateHidden() {
		for (List<WWidget> widgetB : getDropdownWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.setHidden(!getState());
			}
		}
	}

	public List<List<WWidget>> getDropdownWidgets() {
		return dropdownWidgets;
	}

	public void setDropdownWidgets(List<List<WWidget>> dropdownWidgets) {
		this.dropdownWidgets = dropdownWidgets;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			setState(!getState());
			updateHidden();
		}

		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void setLabel(Text label) {
		super.setLabel(label);
		updatePositions();
		updateHidden();
	}

	@Override
	public void align() {
		super.align();

		updatePositions();
	}

	@Override
	public boolean updateFocus(int mouseX, int mouseY) {
		super.updateFocus(mouseX, mouseY);

		setFocus(isWithinBounds(mouseX, mouseY) && getAllWidgets().stream().noneMatch((WWidget::getFocus)));

		return getFocus();
	}

	@Override
	public List<WWidget> getWidgets() {
		List<WWidget> widgets = new ArrayList<>();
		for (List<WWidget> widgetA : getDropdownWidgets()) {
			widgets.addAll(widgetA);
		}
		return widgets;
	}

	@Override
	public int getWidth() {
		return getWidth(!getState() ? 0 : 1);
	}

	@Override
	public int getHeight() {
		return getHeight(!getState() ? 0 : 1);
	}

	@Override
	public void center() {
		int oldX = getX();
		int oldY = getY();

		super.center();

		int newX = getX();
		int newY = getY();

		int offsetX = newX - oldX;
		int offsetY = newY - oldY;

		for (WWidget widget : getWidgets()) {
			widget.setX(widget.getX() + offsetX);
			widget.setY(widget.getY() + offsetY);
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

		BaseRenderer.drawPanel(getX(), getY(), getZ(), getWidth(), getHeight() + 1.75, getStyle().asColor("shadow"), getStyle().asColor("background"), getStyle().asColor("highlight"), getStyle().asColor("outline"));

		if (hasLabel()) {
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6, getStyle().asColor("label"));

			if (getState()) {
				BaseRenderer.drawRectangle(x, y + 16, z, sX, 1, getStyle().asColor("outline"));
				BaseRenderer.drawRectangle(x + 1, y + 17, z, sX - 2, 0.75, getStyle().asColor("shadow"));
			}
		}

		if (getState()) {
			for (List<WWidget> widgetB : getDropdownWidgets()) {
				for (WWidget widgetC : widgetB) {
					widgetC.draw();
				}
			}
		}
	}

	public void updatePositions() {
		int y = getY() + (hasLabel() ? 20 : 8);

		for (List<WWidget> widgetA : getDropdownWidgets()) {
			int x = getX() + 4;
			for (WWidget widgetB : widgetA) {
				widgetB.setX(x);
				widgetB.setY(y);
				x += widgetB.getWidth() + 2;
			}
			y += widgetA.get(0).getHeight() + 2;
		}
	}

	@Override
	public void add(WWidget... widgetArray) {
		getDropdownWidgets().add(Arrays.asList(widgetArray));
		updatePositions();
		updateHidden();
	}

	@Override
	public void remove(WWidget... widgetArray) {
		getDropdownWidgets().remove(Arrays.asList(widgetArray));
		updatePositions();
		updateHidden();
	}

	@Override
	public boolean contains(WWidget... widgetArray) {
		return getDropdownWidgets().containsAll(Arrays.asList(widgetArray));
	}
}
