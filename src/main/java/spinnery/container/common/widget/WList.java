package spinnery.container.common.widget;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;
import spinnery.SpinneryMod;
import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WList extends WWidget {

	public List<List<WWidget>> listWidgets = new ArrayList<>();

	protected String listLabel;

	protected double listSizeY = 0;
	protected double listSizeX = 0;

	protected double entrySizeY = 0;
	protected double entrySizeX = 0;

	protected int listPixelsY = 0;

	protected double kineticScroll = 0;

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
	}

	public String getListLabel() {
		return listLabel;
	}

	public void setListLabel(String listLabel) {
		this.listLabel = listLabel;
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
					widget.setHidden(!(isWithinBounds(widget.getPositionX(), widget.getPositionY())));
				});
			});
		}
		super.onMouseScrolled(mouseX, mouseY, scrollOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		listWidgets.forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseClicked(mouseX, mouseY, mouseButton);
			});
		});
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		listWidgets.forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseReleased(mouseX, mouseY, mouseButton);
			});
		});
		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		listWidgets.forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
			});
		});
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void onMouseMoved(double mouseX, double mouseY) {
		listWidgets.forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseMoved(mouseX, mouseY);
			});
		});
		super.onMouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean scanFocus(double mouseX, double mouseY) {
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

	public void updateHidden() {
		listWidgets.forEach(widgets -> widgets.forEach(widget -> widget.setHidden(!isWithinBounds(widget.getPositionX(), widget.getPositionY()))));
	}

	public void add(WWidget... widgetArray) {
		listWidgets.add(Arrays.asList(widgetArray));
		updateSize();
		updateHidden();
		updatePositions();
	}

	public void remove(WWidget... widgetArray) {
		listWidgets.remove(Arrays.asList(widgetArray));
		updateSize();
		updateHidden();
		updatePositions();
	}

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
	}
}
