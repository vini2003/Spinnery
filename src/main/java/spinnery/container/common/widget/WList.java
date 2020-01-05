package spinnery.container.common.widget;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.container.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WList extends WWidget {
	public List<List<WWidget>> listWidgets = new ArrayList<>();

	public WList(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);
	}

	public List<List<WWidget>> getListWidgets() {
		return listWidgets;
	}

	public void setListWidgets(List<List<WWidget>> listWidgets) {
		this.listWidgets = listWidgets;
	}

	@Override
	public void onMouseScrolled(double mouseX, double mouseY, double scrollOffsetY) {
		if (!isWithinBounds(mouseX, mouseY)) {
			return;
		}

		double scaledOffsetY = scrollOffsetY * 2.5;

		boolean hitTop = getListWidgets().get(0).stream().anyMatch(widget ->
				widget.getPositionY() + scaledOffsetY > getPositionY()
		);

		boolean hitBottom = getListWidgets().get(getListWidgets().size() - 1).stream().anyMatch(widget ->
				widget.getPositionY() + widget.getSizeY() + scaledOffsetY <= getPositionY() + getSizeY() - 3
		);

		if (!hitTop && !hitBottom) {
			getListWidgets().forEach((widgets) -> {
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
		getListWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseClicked(mouseX, mouseY, mouseButton);
			});
		});
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		getListWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseReleased(mouseX, mouseY, mouseButton);
			});
		});
		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		getListWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
			});
		});
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void onMouseMoved(double mouseX, double mouseY) {
		getListWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onMouseMoved(mouseX, mouseY);
			});
		});
		super.onMouseMoved(mouseX, mouseY);
	}

	@Override
	public void onKeyPressed(int keyPressed) {
		getListWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onKeyPressed(keyPressed);
			});
		});
		super.onKeyPressed(keyPressed);
	}

	@Override
	public void onKeyReleased(int keyReleased) {
		getListWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.onKeyReleased(keyReleased);
			});
		});
		super.onKeyReleased(keyReleased);
	}

	@Override
	public boolean scanFocus(double mouseX, double mouseY) {
		getListWidgets().forEach((widgets) -> {
			widgets.forEach((widget) -> {
				widget.scanFocus(mouseX, mouseY);
			});
		});
		return super.scanFocus(mouseX, mouseY);
	}

	public void updatePositions() {
		int y = 0;
		for (int i = 0; i <= getListWidgets().size() - 1; ++i) {
			int x = (int) getPositionX() + 2;
			for (int k = 0; k <= getListWidgets().get(i).size() - 1; ++k) {
				getListWidgets().get(i).get(k).setPositionX(x);
				getListWidgets().get(i).get(k).setPositionY(y);
				x += getListWidgets().get(i).get(k).getSizeX() + 2;
			}
			y += getListWidgets().get(i).get(0).getSizeY() + 2;
		}
	}

	public void updateHidden() {
		getListWidgets().forEach(widgets -> widgets.forEach(widget -> widget.setHidden(!isWithinBounds(widget.getPositionX(), widget.getPositionY()))));
	}

	public void add(WWidget... widgetArray) {
		getListWidgets().add(Arrays.asList(widgetArray));
		updateHidden();
		updatePositions();
	}

	public void remove(WWidget... widgetArray) {
		getListWidgets().remove(Arrays.asList(widgetArray));
		updateHidden();
		updatePositions();
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawPanel(getPositionX() - 4, getPositionY() - 4, getPositionZ() - 1, getSizeX() + 8, getSizeY() + 8, BaseRenderer.SHADOW_DEFAULT, BaseRenderer.PANEL_DEFAULT, BaseRenderer.HILIGHT_DEFUALT, BaseRenderer.OUTLINE_DEFAULT);

		int rawHeight = MinecraftClient.getInstance().window.getHeight();
		double scale = MinecraftClient.getInstance().window.getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (getPositionX() * scale), (int) (rawHeight - (getPositionY() * scale) - (getSizeY() * scale)), (int) (getSizeX() * scale), (int) (getSizeY() * scale));

		getListWidgets().forEach((widgets) -> {
			widgets.forEach(WWidget::drawWidget);
		});

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}
