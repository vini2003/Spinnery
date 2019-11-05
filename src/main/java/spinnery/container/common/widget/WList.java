package spinnery.container.common.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;
import spinnery.SpinneryMod;
import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WList extends WWidget {
	protected WPanel dropdownWPanel;

	public List<List<WWidget>> listWidgets = new ArrayList<>();
	protected int scrollY = 0;

	protected String listLabel;

	protected double listSizeY = 0;
	protected double listSizeX = 0;

	protected double entrySizeY = 0;
	protected double entrySizeX = 0;

	protected int listPixelsY = 0;

	protected double scrollPercentage = 0;

	protected List<WWidget> startMarker;
	protected List<WWidget> endMarker;

	public WList(int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeX, double listSizeY, double entrySizeX, double entrySizeY, WPanel linkedWPanel) {
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
		setDropdownPanel(new WPanel(positionX, positionY + 18, positionZ - 1, (int) (entrySizeX * listSizeX), (int) (entrySizeY * listSizeY)));

		for (int i = 0; i < listSizeX * listSizeY; ++i) {
			getDropdownPanel().getLinkedWidgets().add(new WWidget());
		}
	}

	public static void addSingle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeX, double listSizeY, double entrySizeX, double entrySizeY, WPanel linkedWPanel) {
		linkedWPanel.addWidget(new WList(positionX, positionY, positionZ, sizeX, sizeY, listSizeX, listSizeY, entrySizeX, entrySizeY, linkedWPanel));
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
		if (!isWithinBounds(mouseX, mouseY)) {
			return;
		}

		double scaledOffsetY = scrollOffsetY * 2.5;

		boolean hitTop = listWidgets.get(0).stream().anyMatch(widget ->
				widget.getPositionY() + scaledOffsetY > getPositionY()
		);

		boolean hitBottom = listWidgets.get(listWidgets.size() - 1).stream().anyMatch(widget ->
				widget.getPositionY() + widget.getSizeY() + scaledOffsetY <= getPositionY() + getSizeY() - 3
		);

		if (!hitTop && !hitBottom) {
			listWidgets.forEach((widgets) -> {
				widgets.forEach((widget) -> {
					widget.setPositionY(widget.getPositionY() + scaledOffsetY);
				});
			});
		}
		super.onMouseScrolled(mouseX, mouseY, scrollOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		listWidgets.forEach((widgets) -> {
			widgets.forEach((widget) -> {
				if (widget.isWithinBounds(mouseX, mouseY) && isFocused(mouseX, mouseY)) {
					widget.setHidden(false);
					widget.isFocused(mouseX, mouseY);
					widget.onMouseClicked(mouseX, mouseY, mouseButton);
				} else {
					widget.setHidden(true);
				}
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
		int y = 0;
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

	public void updateSize() {
		listPixelsY = 0;
		listWidgets.forEach(widgets -> widgets.forEach(widget -> listPixelsY += widget.getSizeY() + 2));
	}

	public void add(WWidget... widgetArray) {
		List<WWidget> widgets = Arrays.asList(widgetArray);
		if (Arrays.stream(widgetArray).anyMatch(widget ->  (widget instanceof WSlot))) {
			SpinneryMod.logger.log(Level.WARN, SpinneryMod.LOG_ID + " Illegal operation: Cannot add WSlot to non-WSlotList!");
			return;
		} else {
			listWidgets.add(widgets);
		}
		updateSize();
		updatePositions();
	}

	public void remove(WWidget... widgetArray) {
		listWidgets.remove(Arrays.asList(widgetArray));
		updateSize();
		updatePositions();
	}

	@Override
	public boolean isWithinBounds(double positionX, double positionY) {
		return (positionX <= getPositionX() + getSizeX()
				&& positionX >= getPositionX()
				&& positionY <= getPositionY() + getDropdownPanel().getSizeY()
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
