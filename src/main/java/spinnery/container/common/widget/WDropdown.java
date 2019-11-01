package spinnery.container.common.widget;

import net.minecraft.text.Text;
import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * Represents a dropdown widget.
 */
public class WDropdown extends WWidget {
	protected WPanel dropdownWPanel;

	protected Text dropdownLabel;

	// when clicked
	protected boolean opened = false;

	public WDropdown(WPanel linkedWPanel, int positionX, int positionY, int positionZ, double sizeX, double sizeY, Text dropdownLabel) {
		setPosition(positionX, positionY, positionZ);
		setSize(sizeX, sizeY);

		setLinkedPanel(linkedWPanel);

		setDropdownLabel(dropdownLabel);
		setDropdownPanel(new WPanel(positionX, (int) (positionY + this.sizeY) - 4, positionZ - 1, (int) sizeX, 4));
	}

	public WPanel getDropdownPanel() {
		return dropdownWPanel;
	}

	public void setDropdownPanel(WPanel dropdownWPanel) {
		this.dropdownWPanel = dropdownWPanel;
	}

	public Text getDropdownLabel() {
		return dropdownLabel;
	}

	public void setDropdownLabel(Text dropdownLabel) {
		this.dropdownLabel = dropdownLabel;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (isMovable() && getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			setPositionX(getPositionX() + dragOffsetX);
			setPositionY(getPositionY() + dragOffsetY);

			WPanel dropdown = getDropdownPanel();

			dropdown.setPosition(
					dropdown.getPositionX() + dragOffsetX,
					dropdown.getPositionY() + dragOffsetY
			);

			dropdown.getLinkedWidgets().forEach((widget) -> {
				widget.setPosition(
						widget.getPositionX() + dragOffsetX,
						widget.getPositionY() + dragOffsetY
				);
			});
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			setOpened(!isOpened());
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

	public void addWidget(WWidget child) {
		WPanel dropdown = getDropdownPanel();
		List<WWidget> ddWidgets = dropdown.getLinkedWidgets();

		// if (dropdown.getLinkedWidgets().size() > 0) {
		if (!dropdown.getLinkedWidgets().isEmpty()) {  // same thing as commented line!
			child.setPositionY(ddWidgets.get(ddWidgets.size() - 1).getPositionY() + child.getSizeY());
		}
		else {
			child.setPositionY(dropdown.getPositionY() + dropdown.getSizeY());
		}

		child.setPositionX(getDropdownPanel().getPositionX() + 4);
		dropdown.setSizeY(dropdown.getSizeY() + child.getSizeY() + 4);
		dropdown.addWidget(child);
	}

	public void removeWidget(WWidget child) {
		WPanel dropdown = getDropdownPanel();
		List<WWidget> ddWidgets = dropdown.getLinkedWidgets();

		dropdown.removeWidget(child);
		if (ddWidgets.get(ddWidgets.size() - 1).getSizeX() < dropdown.getSizeX()) {
			dropdown.setSizeX(ddWidgets.get(ddWidgets.size() - 1).getSizeX());
		}
		if (ddWidgets.get(ddWidgets.size() - 1).getSizeY() < dropdown.getSizeY()) {
			dropdown.setSizeY(ddWidgets.get(ddWidgets.size() - 1).getSizeY());
		}
	}

	@Override
	public boolean isWithinBounds(double positionX, double positionY) {
		return (positionX <= getPositionX() + getSizeX()
		     && positionX >= getPositionX()
			 && positionY <= getPositionY() + (isOpened() ? getSizeY()  + getDropdownPanel().getSizeY() : getSizeY())
			 && positionY >= getPositionY());
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), BaseRenderer.SHADOW_DEFAULT, BaseRenderer.PANEL_DEFAULT, BaseRenderer.HILIGHT_DEFUALT, BaseRenderer.OUTLINE_DEFAULT);
		WPanel dropdown = getDropdownPanel();
		if (isOpened()) {
			dropdown.drawPanel();
			dropdown.drawWidget();
		}

		MinecraftClient.getInstance().textRenderer.draw(getDropdownLabel().asFormattedString(), (int) (getPositionX() + (getSizeX() / 16)), (int) (getPositionY() + (getSizeY() / 3F)), 0);
	}
}
