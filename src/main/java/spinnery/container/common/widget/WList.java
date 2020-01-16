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
	public class Theme extends WWidget.Theme {
		transient private WColor shadow;
		transient private WColor background;
		transient private WColor highlight;
		transient private WColor outline;

		@SerializedName("shadow")
		private String rawShadow;

		@SerializedName("background")
		private String rawBackground;

		@SerializedName("highlight")
		private String rawHighlight;

		@SerializedName("outline")
		private String rawOutline;

		public void build() {
			shadow = new WColor(rawShadow);
			background = new WColor(rawBackground);
			highlight = new WColor(rawHighlight);
			outline = new WColor(rawOutline);
		}

		public WColor getShadow() {
			return shadow;
		}

		public WColor getBackground() {
			return background;
		}

		public WColor getHighlight() {
			return highlight;
		}

		public WColor getOutline() {
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
			for (List<WWidget> widgetB : getListWidgets()) {
				for (WWidget widgetC : widgetB) {
					widgetC.setPositionY(widgetC.getPositionY() + scaledOffsetY);
					widgetC.setHidden(!(isWithinBounds(widgetC.getPositionX(), widgetC.getPositionY())));
				}
			}
		}

		super.onMouseScrolled(mouseX, mouseY, scrollOffsetY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.onMouseClicked(mouseX, mouseY, mouseButton);
			}
		}

		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.onMouseReleased(mouseX, mouseY, mouseButton);
			}
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
			}
		}

		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void onMouseMoved(double mouseX, double mouseY) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.onMouseMoved(mouseX, mouseY);
			}
		}

		super.onMouseMoved(mouseX, mouseY);
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.onKeyPressed(keyPressed, character, keyModifier);
			}
		}

		super.onKeyPressed(keyPressed, character, keyModifier);
	}

	@Override
	public void onKeyReleased(int keyReleased) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.onKeyReleased(keyReleased);
			}
		}

		super.onKeyReleased(keyReleased);
	}

	@Override
	public boolean scanFocus(double mouseX, double mouseY) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.scanFocus(mouseX, mouseY);
			}
		}

		setFocus(isWithinBounds(mouseX, mouseY) && getListWidgets().stream().noneMatch((widgets) -> widgets.stream().anyMatch(WWidget::getFocus)));

		return getFocus();
	}

	public void updatePositions() {
		int y = 4;

		for (List<WWidget> widgetA : getListWidgets()) {
			int x = (int) getPositionX() + 4;
			for (WWidget widgetB : widgetA) {
				widgetB.setPositionX(x);
				widgetB.setPositionY(y);
				x += widgetB.getSizeX() + 2;
			}
			y += widgetA.get(0).getSizeY() + 2;
		}
	}

	public void updateHidden() {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.setHidden(!isWithinBounds(widgetC.getPositionX(), widgetC.getPositionY()));
			}
		}
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

		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.drawWidget();
			}
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}
