package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class Dropdown extends Widget {
	protected Panel dropdownPanel;

	protected String dropdownLabel;

	protected boolean state = false;

	public Dropdown(int positionX, int positionY, int positionZ, double sizeX, double sizeY, String dropdownLabel, Panel linkedPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLinkedPanel(linkedPanel);

		setDropdownLabel(dropdownLabel);
		setDropdownPanel(new Panel(positionX, (int) (positionY + this.sizeY) - 4, positionZ - 1, (int) sizeX, 4));
	}

	public Panel getDropdownPanel() {
		return dropdownPanel;
	}

	public void setDropdownPanel(Panel dropdownPanel) {
		this.dropdownPanel = dropdownPanel;
	}

	public String getDropdownLabel() {
		return dropdownLabel;
	}

	public void setDropdownLabel(String dropdownLabel) {
		this.dropdownLabel = dropdownLabel;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (getCanMove() && getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			setPositionX(getPositionX() + dragOffsetX);
			setPositionY(getPositionY() + dragOffsetY);

			getDropdownPanel().setPositionX(getDropdownPanel().getPositionX() + dragOffsetX);
			getDropdownPanel().setPositionY(getDropdownPanel().getPositionY() + dragOffsetY);

			getDropdownPanel().getLinkedWidgets().forEach((widget) -> {
				widget.setPositionX(widget.getPositionX() + dragOffsetX);
				widget.setPositionY(widget.getPositionY() + dragOffsetY);
			});
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			setState(!getState());
		}
		getDropdownPanel().getLinkedWidgets().forEach((widget) -> {
			widget.isFocused(mouseX, mouseY);
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
		});
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean isFocused(double mouseX, double mouseY) {
		setFocus(mouseX > getPositionX()
			  && mouseX < getPositionX() + getSizeX() - 7
			  && mouseY > getPositionY()
			  && mouseY < getPositionY() + getSizeY());
		return getFocus();
	}

	public void addWidget(Widget widget) {
		if (getDropdownPanel().getLinkedWidgets().size() > 0) {
			widget.setPositionY(getDropdownPanel().getLinkedWidgets().get(getDropdownPanel().getLinkedWidgets().size() - 1).getPositionY() + widget.getSizeY());
		}
		if (getDropdownPanel().getLinkedWidgets().size() == 0) {
			widget.setPositionY(getDropdownPanel().getPositionY() + getDropdownPanel().getSizeY());
		}
		widget.setPositionX(getDropdownPanel().getPositionX() + 4);
		getDropdownPanel().setSizeY(getDropdownPanel().getSizeY() + widget.getSizeY() + 1);
		getDropdownPanel().addWidget(widget);
	}

	public void removeWidget(Widget widget) {
		getDropdownPanel().removeWidget(widget);
		if (getDropdownPanel().getLinkedWidgets().get(getDropdownPanel().getLinkedWidgets().size() - 1).getSizeX() < getDropdownPanel().getSizeX()) {
			getDropdownPanel().setSizeX(getDropdownPanel().getLinkedWidgets().get(getDropdownPanel().getLinkedWidgets().size() - 1).getSizeX());
		}
		if (getDropdownPanel().getLinkedWidgets().get(getDropdownPanel().getLinkedWidgets().size() - 1).getSizeY() < getDropdownPanel().getSizeY()) {
			getDropdownPanel().setSizeY(getDropdownPanel().getLinkedWidgets().get(getDropdownPanel().getLinkedWidgets().size() - 1).getSizeY());
		}
	}

	@Override
	public boolean isWithinBounds(double positionX, double positionY) {
		return (positionX <= getPositionX() + getSizeX()
		     && positionX >= getPositionX()
			 && positionY <= getPositionY() + (getState() ? getSizeY()  + getDropdownPanel().getSizeY() : getSizeY())
			 && positionY >= getPositionY());
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), BaseRenderer.SHADOW_DEFAULT, BaseRenderer.PANEL_DEFAULT, BaseRenderer.HILIGHT_DEFUALT, BaseRenderer.OUTLINE_DEFAULT);
		if (getState()) {
			getDropdownPanel().drawPanel();
			getDropdownPanel().drawWidget();
		}
		MinecraftClient.getInstance().textRenderer.draw(getDropdownLabel(), (int) (getPositionX() + (getSizeX() / 16)), (int) (getPositionY() + (getSizeY() / 3F)), 0);
	}
}
