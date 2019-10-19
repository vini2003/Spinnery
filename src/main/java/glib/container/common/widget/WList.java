package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WList extends WWidget {
	protected WPanel dropdownWPanel;

	protected LinkedList<WWidget> listWidgets = new LinkedList<>();
	protected int scrollY = 0;

	protected String listLabel;

	protected double listSizeY = 0;
	protected double entrySizeY = 0;

	protected WWidget startMarker;
	protected WWidget endMarker;

	public WList(int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeY, double entrySizeY, WPanel linkedWPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		this.listSizeY = listSizeY;
		this.entrySizeY = entrySizeY;

		setLinkedWPanel(linkedWPanel);

		setListLabel(listLabel);
		setDropdownPanel(new WPanel(positionX, positionY, positionZ - 1, (int) sizeX, 4));
		for (int i = 0; i < listSizeY; ++i) {
			getDropdownPanel().getLinkedWidgets().add(new WWidget());
		}
	}

	public static void addSingle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeY, double entrySizeY, WPanel linkedWPanel) {
		linkedWPanel.addWidget(new WList(positionX, positionY, positionZ, sizeX, sizeY, listSizeY, entrySizeY, linkedWPanel));
	}

	public WPanel getDropdownPanel() {
		return dropdownWPanel;
	}

	public void setDropdownPanel(WPanel dropdownWPanel) {
		this.dropdownWPanel = dropdownWPanel;
	}

	public String getListLabel() {
		return listLabel;
	}

	public void setListLabel(String listLabel) {
		this.listLabel = listLabel;
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
	public void onMouseScrolled(double mouseX, double mouseY, double scrollOffsetY) {
		if (scrollOffsetY > 0) {
			if (listWidgets.getLast() != endMarker) {
				listWidgets.addFirst(listWidgets.getLast());
				listWidgets.removeLast();

				for (int i = 0; i <= getDropdownPanel().getLinkedWidgets().size() - 1; ++i) {
					getDropdownPanel().getLinkedWidgets().set(i, listWidgets.get(i));
					getDropdownPanel().getLinkedWidgets().get(i).setPositionY(dropdownWPanel.getPositionY() + 3 + i * entrySizeY);
				}
			}
		} else {
			if (listWidgets.get((int) listSizeY - 1) != endMarker) {
				listWidgets.add(listWidgets.getFirst());
				listWidgets.removeFirst();

				for (int i = 0; i <= getDropdownPanel().getLinkedWidgets().size() - 1; ++i) {
					getDropdownPanel().getLinkedWidgets().set(i, listWidgets.get(i));
					getDropdownPanel().getLinkedWidgets().get(i).setPositionY(dropdownWPanel.getPositionY() + 3 + i * entrySizeY);
				}
			}
		}
		super.onMouseScrolled(mouseX, mouseY, scrollOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		getDropdownPanel().getLinkedWidgets().forEach((widget) -> {
			widget.isFocused(mouseX, mouseY);
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
		});
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean isFocused(double mouseX, double mouseY) {
		setFocus(mouseX > getPositionX()
				&& mouseX < getPositionX() + getSizeX()
				&& mouseY > getPositionY()
				&& mouseY < getPositionY() + getSizeY());
		return getFocus();
	}

	public void addWidget(WWidget widget) {
		widget.setPositionY(getPositionY() + entrySizeY * listWidgets.size());
		widget.setPositionX(getPositionX() + 4);
		if (listWidgets.size() == 0) {
			startMarker = widget;
		} else {
			endMarker = widget;
		}
		listWidgets.add(widget);
	}

	public void removeWidget(WWidget widget) {
		listWidgets.remove(widget);
		getDropdownPanel().getLinkedWidgets().remove(getLinkedWPanel().getLinkedWidgets().size() - 1);
	}

	@Override
	public boolean isWithinBounds(double positionX, double positionY) {
		return (positionX <= getPositionX() + getSizeX()
				&& positionX >= getPositionX()
				&& positionY <= getPositionY() + getDropdownPanel().getSizeY()
				&& positionY >= getPositionY());
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawPanel(getPositionX(), getPositionY(), getPositionZ() - 1, getSizeX(), getSizeY(), BaseRenderer.SHADOW_DEFAULT, BaseRenderer.PANEL_DEFAULT, BaseRenderer.HILIGHT_DEFUALT, BaseRenderer.OUTLINE_DEFAULT);

		getDropdownPanel().drawPanel();
		getDropdownPanel().drawWidget();

		//MinecraftClient.getInstance().textRenderer.draw(getListLabel(), (int) (getPositionX() + (getSizeX() / 16)), (int) (getPositionY() + (getSizeY() / 3F)), 0);
	}
}
