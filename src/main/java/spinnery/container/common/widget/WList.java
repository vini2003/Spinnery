package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WList extends WWidget {
	public List<List<WWidget>> listWidgets = new ArrayList<>();
	WList.Theme drawTheme;

	public WList(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");
	}

	public List<List<WWidget>> getListWidgets() {
		return listWidgets;
	}

	public void setListWidgets(List<List<WWidget>> listWidgets) {
		this.listWidgets = listWidgets;
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
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.onMouseReleased(mouseX, mouseY, mouseButton);
			}
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
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
	public void onMouseScrolled(double mouseX, double mouseY, double scrollOffsetY) {
		if (! isWithinBounds(mouseX, mouseY) || getListWidgets().size() == 0) {
			return;
		}

		double scaledOffsetY = scrollOffsetY * 2.5;

		boolean hitTop = getListWidgets().get(0).stream().anyMatch(widget ->
				widget.getPositionY() + scaledOffsetY > getPositionY() + 4
		);

		boolean hitBottom = getListWidgets().get(getListWidgets().size() - 1).stream().anyMatch(widget ->
				widget.getPositionY() + widget.getSizeY() + scaledOffsetY <= getPositionY() + getSizeY() - 6
		);

		if ((scaledOffsetY > 0 && ! hitTop) || (scaledOffsetY < 0 && ! hitBottom)) {
			for (List<WWidget> widgetB : getListWidgets()) {
				for (WWidget widgetC : widgetB) {
					widgetC.setPositionY(widgetC.getPositionY() + scaledOffsetY);
					widgetC.setHidden(! (isWithinBounds(widgetC.getPositionX(), widgetC.getPositionY())));
				}
			}
		}

		super.onMouseScrolled(mouseX, mouseY, scrollOffsetY);
	}

	@Override
	public void setTheme(String theme) {
		super.setTheme(theme);
		drawTheme = ResourceRegistry.get(getTheme()).getWListTheme();
	}

	@Override
	public void setLabel(String label) {
		super.setLabel(label);
		updatePositions();
		updateHidden();
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

	@Override
	public void draw() {
		double x = getPositionX();
		double y = getPositionY();
		double z = getPositionZ();

		double sX = getSizeX();
		double sY = getSizeY();

		BaseRenderer.drawPanel(x, y, z, sX, sY, drawTheme.getShadow(), drawTheme.getBackground(), drawTheme.getHighlight(), drawTheme.getOutline());

		if (hasLabel()) {
			BaseRenderer.getTextRenderer().drawWithShadow(getLabel(), (int) (x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel()) / 2), (int) (positionY + 6), drawTheme.getLabel().RGB);
			BaseRenderer.drawRectangle(positionX, positionY + 16, positionZ, sizeX, 1, drawTheme.getOutline());
			BaseRenderer.drawRectangle(positionX + 1, positionY + 17, positionZ, sizeX - 2, 0.75, drawTheme.getShadow());
		}

		int rawHeight = MinecraftClient.getInstance().window.getHeight();
		double scale = MinecraftClient.getInstance().window.getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y - 4) * scale) - (sY * scale)), (int) (sX * scale), (int) ((sY - 8 - (hasLabel() ? 13.75 : 0)) * scale));

		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.draw();
			}
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public void updatePositions() {
		int y = hasLabel() ? (int) (positionY + 20) : 4;

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
				widgetC.setHidden(! isWithinBounds(widgetC.getPositionX(), widgetC.getPositionY()));
			}
		}
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

	public class Theme extends WWidget.Theme {
		transient private WColor shadow;
		transient private WColor background;
		transient private WColor highlight;
		transient private WColor outline;
		transient private WColor label;

		@SerializedName("shadow")
		private String rawShadow;

		@SerializedName("background")
		private String rawBackground;

		@SerializedName("highlight")
		private String rawHighlight;

		@SerializedName("outline")
		private String rawOutline;

		@SerializedName("label")
		private String rawLabel;

		public void build() {
			shadow = new WColor(rawShadow);
			background = new WColor(rawBackground);
			highlight = new WColor(rawHighlight);
			outline = new WColor(rawOutline);
			label = new WColor(rawLabel);
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

		public WColor getLabel() {
			return label;
		}
	}
}
