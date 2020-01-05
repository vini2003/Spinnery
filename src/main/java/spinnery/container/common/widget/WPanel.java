package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import spinnery.container.common.BaseContainer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class WPanel extends WWidget {
	protected BaseContainer linkedContainer;

	protected List<WWidget> linkedWWidgets = new ArrayList<>();

	public WPanel(BaseContainer linkedContainer) {
		setLinkedContainer(linkedContainer);
	}

	public WPanel(int positionX, int positionY, int positionZ, int sizeX, int sizeY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);
	}

	public WPanel(int positionX, int positionY, int positionZ, int sizeX, int sizeY, BaseContainer linkedContainer) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLinkedContainer(linkedContainer);
	}

	public BaseContainer getLinkedContainer() {
		return linkedContainer;
	}

	public void setLinkedContainer(BaseContainer linkedContainer) {
		this.linkedContainer = linkedContainer;
	}

	public List<WWidget> getLinkedWidgets() {
		return linkedWWidgets;
	}

	public void setLinkedWWidgets(List<WWidget> linkedWWidgets) {
		this.linkedWWidgets = linkedWWidgets;
	}

	public void add(WWidget... WWidgets) {
		for (WWidget WWidget : WWidgets) {
			if (!this.getLinkedWidgets().contains(WWidget)) {
				WWidget.setLinkedPanel(this);
				getLinkedWidgets().add(WWidget);
			}
		}
	}

	public void remove(WWidget... WWidgets) {
		for (WWidget WWidget : WWidgets) {
			if (this.getLinkedWidgets().contains(WWidget)) {
				getLinkedWidgets().remove(WWidget);
			}
		}
	}

	@Override
	public void onKeyPressed(int keyPressed) {
		if (getFocus()) {
			switch (keyPressed) {
				case GLFW.GLFW_KEY_KP_SUBTRACT:
					setPositionX((int) Math.round(getPositionX() + 1));
					break;
				case GLFW.GLFW_KEY_KP_DIVIDE:
					setPositionX((int) Math.round(getPositionX() - 1));
					break;
				case GLFW.GLFW_KEY_KP_EQUAL:
					setPositionY((int) Math.round(getPositionY() + 1));
					break;
				case GLFW.GLFW_KEY_KP_8:
					setPositionY((int) Math.round(getPositionY() - 1));
					break;
			}
		}
		super.onKeyPressed(keyPressed);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (getCanMove() && getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			setPositionX(getPositionX() + dragOffsetX);
			setPositionY(getPositionY() + dragOffsetY);
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	public void drawPanel() {
		BaseRenderer.drawPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), BaseRenderer.SHADOW_DEFAULT, BaseRenderer.PANEL_DEFAULT, BaseRenderer.HILIGHT_DEFUALT, BaseRenderer.OUTLINE_DEFAULT);
	}

	@Override
	public void drawWidget() {
		getLinkedWidgets().forEach(WWidget::drawWidget);
	}

	@Override
	public void tick() {
		linkedWWidgets.forEach(WWidget::tick);
	}
}
