package spinnery.container.common.widget;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import spinnery.SpinneryMod;
import spinnery.container.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WSlotList extends WWidget {
	protected WPanel dropdownWPanel;

	public LinkedList<List<WWidget>> listWidgets = new LinkedList<>();
	protected int scrollY = 0;

	protected String listLabel;

	protected double listSizeY = 0;
	protected double listSizeX = 0;

	protected double entrySizeY = 0;
	protected double entrySizeX = 0;

	protected int listPixelsY = 0;

	protected int scrollTotal = 0;
	protected int scrollCurrent = 0;

	protected List<WWidget> startMarker;
	protected List<WWidget> endMarker;

	public WSlotList(int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeX, double listSizeY, double entrySizeX, double entrySizeY, WPanel linkedWPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		this.listSizeY = listSizeY;
		this.listSizeX = listSizeX;

		this.entrySizeY = entrySizeY;
		this.entrySizeX = entrySizeX;

		setLinkedPanel(linkedWPanel);

		setListLabel(listLabel);
	}

	public static void addSingle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeX, double listSizeY, double entrySizeX, double entrySizeY, WPanel linkedWPanel) {
		linkedWPanel.addWidget(new WSlotList(positionX, positionY, positionZ, sizeX, sizeY, listSizeX, listSizeY, entrySizeX, entrySizeY, linkedWPanel));
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
			if (scrollCurrent > 0) {
				listWidgets.addFirst(listWidgets.getLast());
				listWidgets.removeLast();
				--scrollCurrent;
			}
		} else {
			if (scrollCurrent < scrollTotal - getSizeY() / 18) {
				listWidgets.addLast(listWidgets.getFirst());
				listWidgets.removeFirst();
				++scrollCurrent;
			}
		}

		updateHidden();
		updatePositions();

		super.onMouseScrolled(mouseX, mouseY, scrollOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		listWidgets.forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.isFocused(mouseX, mouseY);
				widget.onMouseClicked(mouseX, mouseY, mouseButton);
			});
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

	public void updatePositions() {
		int y = (int) getPositionY();
		for (int i = 0; i <= listWidgets.size() -1; ++i) {
			int x = (int) getPositionX() + 2;
			for (int k = 0; k <= listWidgets.get(i).size() - 1; ++k) {
				listWidgets.get(i).get(k).setPositionX(x);
				listWidgets.get(i).get(k).setPositionY(y);
				x += listWidgets.get(i).get(k).getSizeX() + 2;
			}
			y += listWidgets.get(i).get(0).getSizeY() + 2;
		}
	}

	public void updateHidden() {
		listWidgets.forEach(widgets -> widgets.forEach(widget -> widget.setHidden(true)));
		for (int i = (int) Math.floor(getSizeY() / (20)); i >= 0; --i) {
			if (listWidgets.size() - 1 >= i) {
				listWidgets.get(i).forEach(widget -> widget.setHidden(false));
			}
		}
	}

	public void updateSize() {
		listPixelsY = 0;
		listWidgets.forEach(widgets -> widgets.forEach(widget -> listPixelsY += widget.getSizeY() + 2));
	}

	public void add(WWidget... widgetArray) {
		List<WWidget> widgets = Arrays.asList(widgetArray);
		if (Arrays.stream(widgetArray).anyMatch(widget ->  !(widget instanceof WSlot))) {
			SpinneryMod.logger.log(Level.WARN, SpinneryMod.LOG_ID + " Illegal operation: Cannot add non-WSlot to WSlotList!");
			return;
		} else {
			listWidgets.add(widgets);
		}
		++scrollTotal;
		updateSize();
		updateHidden();
		updatePositions();
	}

	public void remove(WWidget... widgetArray) {
		listWidgets.remove(Arrays.asList(widgetArray));
		--scrollTotal;
		updateSize();
		updateHidden();
		updatePositions();
	}

	@Override
	public boolean isWithinBounds(double positionX, double positionY) {
		return (positionX <= getPositionX() + getSizeX()
				&& positionX >= getPositionX()
				&& positionY <= getPositionY() + getSizeY()
				&& positionY >= getPositionY());
	}

	/**
	 * If is within widget *and* is within list.
	 * GlScissors.
	 */

	@Override
	public void drawWidget() {
		BaseRenderer.drawPanel(getPositionX() - 4, getPositionY() - 4, getPositionZ() - 1, getSizeX() + 8, getSizeY() + 8, BaseRenderer.SHADOW_DEFAULT, BaseRenderer.PANEL_DEFAULT, BaseRenderer.HILIGHT_DEFUALT, BaseRenderer.OUTLINE_DEFAULT);

		final int rawHeight = MinecraftClient.getInstance().window.getHeight();
		final double scale = MinecraftClient.getInstance().window.getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (getPositionX() * scale), (int) (rawHeight - (getPositionY() * scale) - (getSizeY() * scale)), (int) (getSizeX() * scale), (int) (getSizeY() * scale));

		listWidgets.forEach((widgets) -> {
			widgets.forEach(WWidget::drawWidget);
		});

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		//getLinkedPanel().drawPanel();
		//getDropdownPanel().drawPanel();
		//getDropdownPanel().drawWidget();

		//MinecraftClient.getInstance().textRenderer.draw(getListLabel(), (int) (getPositionX() + (getSizeX() / 16)), (int) (getPositionY() + (getSizeY() / 3F)), 0);
	}
}
