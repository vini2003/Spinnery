package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WDropdown extends WAbstractWidget implements WModifiableCollection {
	public List<List<WAbstractWidget>> dropdownWidgets = new ArrayList<>();
	protected boolean state = false;
	protected WSize dropdownSize;

	@Override
	public void onLayoutChange() {
		updatePositions();
		updateHidden();
	}

	public void updateHidden() {
		for (List<WAbstractWidget> widgetB : getDropdownWidgets()) {
			for (WAbstractWidget widgetC : widgetB) {
				widgetC.setHidden(!getState());
			}
		}
	}

	public List<List<WAbstractWidget>> getDropdownWidgets() {
		return dropdownWidgets;
	}

	public <W extends WDropdown> W setDropdownWidgets(List<List<WAbstractWidget>> dropdownWidgets) {
		this.dropdownWidgets = dropdownWidgets;
		return (W) this;
	}

	public boolean getState() {
		return state;
	}

	public <W extends WDropdown> W setState(boolean state) {
		this.state = state;
		return (W) this;
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
	public void align() {
		super.align();
		updatePositions();
	}

	@Override
	public boolean updateFocus(int mouseX, int mouseY) {
		super.updateFocus(mouseX, mouseY);

		setFocus(isWithinBounds(mouseX, mouseY) && getAllWidgets().stream().noneMatch((WAbstractWidget::getFocus)));

		return getFocus();
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		Set<WAbstractWidget> widgets = new LinkedHashSet<>();
		for (List<WAbstractWidget> widgetA : getDropdownWidgets()) {
			widgets.addAll(widgetA);
		}
		return widgets;
	}

	@Override
	public int getWidth() {
		return Math.max(super.getWidth(), state ? dropdownSize.getWidth() : 0);
	}

	@Override
	public int getHeight() {
		return super.getHeight() + (state ? dropdownSize.getHeight() : 0);
	}

	public WSize getDropdownSize() {
		return dropdownSize;
	}

	public <W extends WDropdown> W setDropdownSize(WSize dropdownSize) {
		this.dropdownSize = dropdownSize;
		return (W) this;
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

		for (WAbstractWidget widget : getWidgets()) {
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
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + sX / 2 - TextRenderer.width(getLabel()) / 2, y + 6, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();

			if (getState()) {
				BaseRenderer.drawRectangle(x, y + 16, z, sX, 1, getStyle().asColor("outline"));
				BaseRenderer.drawRectangle(x + 1, y + 17, z, sX - 2, 0.75, getStyle().asColor("shadow"));
			}
		}

		if (getState()) {
			for (WAbstractWidget widgetC : getAllWidgets()) {
				widgetC.draw();
			}
		}
	}

	public void updatePositions() {
		int y = getY() + (hasLabel() ? 20 : 8);

		for (List<WAbstractWidget> widgetA : getDropdownWidgets()) {
			int x = getX() + 4;
			for (WAbstractWidget widgetB : widgetA) {
				widgetB.setX(x);
				widgetB.setY(y);
				x += widgetB.getWidth() + 2;
			}
			y += widgetA.get(0).getHeight() + 2;
		}
	}

	@Override
	public void add(WAbstractWidget... widgetArray) {
		getDropdownWidgets().add(Arrays.asList(widgetArray));
		updatePositions();
		updateHidden();
	}

	@Override
	public void remove(WAbstractWidget... widgetArray) {
		getDropdownWidgets().remove(Arrays.asList(widgetArray));
		updatePositions();
		updateHidden();
	}

	@Override
	public boolean contains(WAbstractWidget... widgetArray) {
		return getDropdownWidgets().containsAll(Arrays.asList(widgetArray));
	}
}
