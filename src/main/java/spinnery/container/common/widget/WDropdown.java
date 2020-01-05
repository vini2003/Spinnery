package spinnery.container.common.widget;

import net.minecraft.util.Pair;
import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WDropdown extends WWidget {
	public List<List<WWidget>> dropdownWidgets = new ArrayList<>();
	int[][] sizes = new int[2][2];

	protected boolean state = false;

	public WDropdown(WAnchor anchor, int positionX, int positionY, int positionZ, int sizeX1, int sizeY1, int sizeX2, int sizeY2, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizes(sizeX1, sizeY1, sizeX2, sizeY2);

		updateHidden();
	}


	@Override
	public double getSizeX() {
		return sizes[!getState() ? 0 : 1][0];
	}

	@Override
	public double getSizeY() {
		return sizes[!getState() ? 0 : 1][1];
	}

	public void setDropdownWidgets(List<List<WWidget>> dropdownWidgets) {
		this.dropdownWidgets = dropdownWidgets;
	}

	public List<List<WWidget>> getDropdownWidgets() {
		return dropdownWidgets;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void setSizes(int sizeX1, int sizeY1, int sizeX2, int sizeY2) {
		sizes = new int[][]{{sizeX1, sizeY1}, {sizeX2, sizeY2}};
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			setState(!getState());
			updateHidden();
		} else {
			getDropdownWidgets().forEach((widgets) -> {
				widgets.forEach((widget) -> {
					widget.onMouseClicked(mouseX, mouseY, mouseButton);
				});
			});
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		getDropdownWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseReleased(mouseX, mouseY, mouseButton);
			});
		});
		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		getDropdownWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
			});
		});
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void onMouseMoved(double mouseX, double mouseY) {
		getDropdownWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseMoved(mouseX, mouseY);
			});
		});
		super.onMouseMoved(mouseX, mouseY);
	}

	@Override
	public void onKeyPressed(int keyPressed) {
		getDropdownWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onKeyPressed(keyPressed);
			});
		});
		super.onKeyPressed(keyPressed);
	}

	@Override
	public void onKeyReleased(int keyReleased) {
		getDropdownWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onKeyReleased(keyReleased);
			});
		});
		super.onKeyReleased(keyReleased);
	}

	@Override
	public boolean scanFocus(double mouseX, double mouseY) {
		getDropdownWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.scanFocus(mouseX, mouseY);
			});
		});

		setFocus(isWithinBounds(mouseX, mouseY) && !getDropdownWidgets().stream().anyMatch((widgets) -> widgets.stream().anyMatch(WWidget::getFocus)));

		return getFocus();
	}

	public void updatePositions() {
		int y = sizes[0][1] + 2;
		for (int i = 0; i <= getDropdownWidgets().size() - 1; ++i) {
			int x = (int) getPositionX() + 4;
			for (int k = 0; k <= getDropdownWidgets().get(i).size() - 1; ++k) {
				getDropdownWidgets().get(i).get(k).setPositionX(x);
				getDropdownWidgets().get(i).get(k).setPositionY(y);
				x += getDropdownWidgets().get(i).get(k).getSizeX() + 2;
			}
			y += getDropdownWidgets().get(i).get(0).getSizeY() + 2;
		}
	}

	public void updateHidden() {
		getDropdownWidgets().forEach(widgets -> widgets.forEach(widget -> widget.setHidden(!getState())));
	}

	public void add(WWidget... widgetArray) {
		getDropdownWidgets().add(Arrays.asList(widgetArray));
		updateHidden();
		updatePositions();
	}

	public void remove(WWidget... widgetArray) {
		getDropdownWidgets().remove(Arrays.asList(widgetArray));
		updateHidden();
		updatePositions();
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), BaseRenderer.SHADOW_DEFAULT, BaseRenderer.PANEL_DEFAULT, BaseRenderer.HILIGHT_DEFUALT, BaseRenderer.OUTLINE_DEFAULT);
		if (getState()) {
			getDropdownWidgets().forEach(widgets -> widgets.forEach(WWidget::drawWidget));
		}
	}
}
