package spinnery.container.common.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL11;
import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class WList extends WWidget {
	protected WPanel dropdownWPanel;

	protected List<List<WWidget>> listWidgets = new ArrayList<>();
	protected int scrollY = 0;

	protected Text listLabel;

	protected double listSizeY = 0;
	protected double listSizeX = 0;

	protected double entrySizeY = 0;
	protected double entrySizeX = 0;

	protected int listPixelsY = 0;

	protected double scrollPercentage = 0;

	protected List<WWidget> startMarker;
	protected List<WWidget> endMarker;

	public WList(WPanel linkedWPanel, int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeX, double listSizeY, double entrySizeX, double entrySizeY) {
		setPosition(positionX, positionY, positionZ);
		setSize(sizeX, sizeY);

		this.listSizeY = listSizeY;
		this.listSizeX = listSizeX;

		this.entrySizeY = entrySizeY;
		this.entrySizeX = entrySizeX;

		setLinkedPanel(linkedWPanel);

		// setListLabel(listLabel);

		WPanel dropdown = new WPanel(positionX, positionY + 18, positionZ - 1, (int) (entrySizeX * listSizeX), (int) (entrySizeY * listSizeY));
		setDropdownPanel(dropdown);
		for (int i = 0; i < listSizeX * listSizeY; ++i) {
			dropdown.getLinkedWidgets().add(new WWidget());
		}
	}

	public static void addSingle(WPanel linkedWPanel, int positionX, int positionY, int positionZ, double sizeX, double sizeY, double listSizeX, double listSizeY, double entrySizeX, double entrySizeY) {
		linkedWPanel.addWidget(new WList(linkedWPanel, positionX, positionY, positionZ, sizeX, sizeY, listSizeX, listSizeY, entrySizeX, entrySizeY));
	}

	public WPanel getDropdownPanel() {
		return dropdownWPanel;
	}

	public void setDropdownPanel(WPanel dropdownWPanel) {
		this.dropdownWPanel = dropdownWPanel;
	}

	public Text getListLabel() {
		return listLabel;
	}

	public void setListLabel(Text listLabel) {
		this.listLabel = listLabel;
	}

	public List<List<WWidget>> getListWidgets() { return this.listWidgets; }

	public int getListPixelsY() { return this.listPixelsY; }
	public void setListPixelsY(int value) { this.listPixelsY = value; }

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (isMovable() && getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			setPositionX(getPositionX() + dragOffsetX);
			setPositionY(getPositionY() + dragOffsetY);

			WPanel dropdown = getDropdownPanel();
			dropdown.setPositionX(dropdown.getPositionX() + dragOffsetX);
			dropdown.setPositionY(dropdown.getPositionY() + dragOffsetY);

			dropdown.getLinkedWidgets().forEach((widget) -> {
				widget.setPositionX(widget.getPositionX() + dragOffsetX);
				widget.setPositionY(widget.getPositionY() + dragOffsetY);
			});
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void onMouseScrolled(double mouseX, double mouseY, double scrollOffsetY) {
		double finalScrollOffsetY = scrollOffsetY * 2.5;

		List<List<WWidget>> widgets = getListWidgets();

		boolean cantScroll = widgets.get(0).stream().anyMatch((widget) -> widget.getPositionY() + finalScrollOffsetY > getPositionY());
		boolean cantScroll2 = widgets.get(widgets.size() - 1).stream().anyMatch((widget) -> widget.getPositionY() + widget.getSizeY() + finalScrollOffsetY <= getPositionY() + getSizeY() - 3);

		if (!cantScroll && !cantScroll2) {
			widgets.forEach((subwidgets) -> {
				subwidgets.forEach((widget) -> {
					widget.setPositionY(widget.getPositionY() + finalScrollOffsetY);
				});
			});
		}
		super.onMouseScrolled(mouseX, mouseY, scrollOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		getListWidgets().forEach((widgets) -> {
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
		List<List<WWidget>> widgets = getListWidgets();

		for (int i = 0; i <= widgets.size() -1; ++i) {
			int x = (int) getPositionX() + 2;
			for (int k = 0; k <= widgets.get(i).size() - 1; ++k) {
				widgets.get(i).get(k).setPositionX(x);
				widgets.get(i).get(k).setPositionY(y);
				x += widgets.get(i).get(k).getSizeX() + 2;
			}
			y += widgets.get(i).get(0).getSizeY() + 2;
		}
	}

	public void addWidget(WWidget... widget) {
		List<WWidget> widgets = Arrays.asList(widget);

		// potential better solution:
		//double toIncrement =
		//		widgets.stream().map((tempWidget) -> (2 + tempWidget.getSizeY())).mapToDouble((i)
		//		-> i).sum();
		//int prevSize = getListPixelsY();
		//setListPixelsY(prevSize + (int)toIncrement);

		widgets.forEach((temporaryWidget) -> {
			listPixelsY += (2 + temporaryWidget.getSizeY());
		});
		listWidgets.add(widgets);
		updatePositions();
	}

	public void removeWidget(WWidget... widget) {
		getListWidgets().remove(Arrays.asList(widget));
		getDropdownPanel().getLinkedWidgets().remove(getLinkedPanel().getLinkedWidgets().size() - 1);
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

		getListWidgets().forEach((widgets) -> {
			widgets.forEach(WWidget::drawWidget);
		});

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		//getLinkedPanel().drawPanel();
		//getDropdownPanel().drawPanel();
		//getDropdownPanel().drawWidget();

		//MinecraftClient.getInstance().textRenderer.draw(getListLabel(), (int) (getPositionX() + (getSizeX() / 16)), (int) (getPositionY() + (getSizeY() / 3F)), 0);
	}
}
