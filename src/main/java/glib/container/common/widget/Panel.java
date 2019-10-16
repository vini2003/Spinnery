package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import glib.container.common.BaseContainer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Panel extends Widget {
	protected BaseContainer linkedContainer;

	protected List<Widget> linkedWidgets = new ArrayList<>();

	public Panel(BaseContainer linkedContainer) {
		setLinkedContainer(linkedContainer);
	}

	public Panel(int positionX, int positionY, int positionZ, int sizeX, int sizeY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);
	}

	public Panel(int positionX, int positionY, int positionZ, int sizeX, int sizeY, BaseContainer linkedContainer) {
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

	public List<Widget> getLinkedWidgets() {
		return linkedWidgets;
	}

	public void setLinkedWidgets(List<Widget> linkedWidgets) {
		this.linkedWidgets = linkedWidgets;
	}

	public void addWidget(Widget... widgets) {
		for (Widget widget : widgets) {
			if (!this.getLinkedWidgets().contains(widget)) {
				widget.setLinkedPanel(this);
				getLinkedWidgets().add(widget);
			}
		}
	}

	public void removeWidget(Widget... widgets) {
		for (Widget widget : widgets) {
			if (this.getLinkedWidgets().contains(widget)) {
				getLinkedWidgets().remove(widget);
			}
		}
	}

	public void alignWithContainerEdge() {
		this.setPositionX(MinecraftClient.getInstance().window.getScaledWidth() / 2D - this.getSizeX() / 2);
		this.setPositionY(MinecraftClient.getInstance().window.getScaledHeight() / 2D - this.getSizeY() / 2);
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
		getLinkedWidgets().forEach(Widget::drawWidget);
	}

	@Override
	public void tick() {
		linkedWidgets.forEach(Widget::tick);
	}
}
