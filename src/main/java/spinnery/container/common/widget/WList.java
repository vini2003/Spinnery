package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.mutable.MutableInt;
import org.lwjgl.opengl.GL11;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WList extends WWidget {
	public class Theme {
		@SerializedName("shadow")
		private String shadow;

		@SerializedName("background")
		private String background;

		@SerializedName("highlight")
		private String highlight;

		@SerializedName("outline")
		private String outline;

		public String getShadow() {
			return shadow;
		}

		public String getBackground() {
			return background;
		}

		public String getHighlight() {
			return highlight;
		}

		public String getOutline() {
			return outline;
		}
	}

	public List<List<WWidget>> listWidgets = new ArrayList<>();
	public MutableInt publicTotal = new MutableInt(0);
	public MutableInt publicCurrent = new MutableInt(0);

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
		if (!isWithinBounds(mouseX, mouseY) || getListWidgets().size() == 0) {
			return;
		}

		double scaledOffsetY = scrollOffsetY * 2.5;

		boolean hitTop = getListWidgets().get(0).stream().anyMatch(widget ->
				widget.getPositionY() + scaledOffsetY > getPositionY() + 4
		);

		boolean hitBottom = getListWidgets().get(getListWidgets().size() - 1).stream().anyMatch(widget ->
				widget.getPositionY() + widget.getSizeY() + scaledOffsetY <= getPositionY() + getSizeY() - 6
		);

		if ((scaledOffsetY > 0 && !hitTop) || (scaledOffsetY < 0 && !hitBottom)) {
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

		setFocus(isWithinBounds(mouseX, mouseY) && !getListWidgets().stream().anyMatch((widgets) -> widgets.stream().anyMatch(WWidget::getFocus)));

		return getFocus();
	}

	public void updatePositions() {
		int y = 4;
		for (int i = 0; i <= getListWidgets().size() - 1; ++i) {
			int x = (int) getPositionX() + 4;
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
		publicTotal.add(widgetArray.length);
		getListWidgets().add(Arrays.asList(widgetArray));
		updateHidden();
		updatePositions();
	}

	public void remove(WWidget... widgetArray) {
		publicTotal.add(widgetArray.length);
		getListWidgets().remove(Arrays.asList(widgetArray));
		updateHidden();
		updatePositions();
	}

	@Override
	public void drawWidget() {
		WList.Theme drawTheme = ResourceRegistry.get(getTheme()).getWListTheme();

		BaseRenderer.drawPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), drawTheme.getShadow(), drawTheme.getBackground(), drawTheme.getHighlight(), drawTheme.getOutline());

		int rawHeight = MinecraftClient.getInstance().window.getHeight();
		double scale = MinecraftClient.getInstance().window.getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (getPositionX() * scale), (int) (rawHeight - ((getPositionY() - 4) * scale) - (getSizeY() * scale)), (int) (getSizeX() * scale), (int) ((getSizeY() - 8) * scale));

		getListWidgets().forEach((widgets) -> {
			widgets.forEach(WWidget::drawWidget);
		});

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}
