package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import spinnery.container.common.BaseContainer;
import org.lwjgl.glfw.GLFW;
import spinnery.registry.ResourceRegistry;

import java.util.ArrayList;
import java.util.List;

public class WPanel extends WWidget {
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

		public WColor getLabel() { return label; }
	}

	WPanel.Theme drawTheme;

	protected BaseContainer linkedContainer;

	protected List<WWidget> linkedWWidgets = new ArrayList<>();

	public WPanel(BaseContainer linkedContainer) {
		setLinkedContainer(linkedContainer);
	}

	public WPanel(int positionX, int positionY, int positionZ, int sizeX, int sizeY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");
	}

	public WPanel(int positionX, int positionY, int positionZ, int sizeX, int sizeY, BaseContainer linkedContainer) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");

		setLinkedContainer(linkedContainer);
	}

	public BaseContainer getLinkedContainer() {
		return linkedContainer;
	}

	public void setLinkedContainer(BaseContainer linkedContainer) {
		this.linkedContainer = linkedContainer;
	}

	public List<WWidget> getLinkedWidgets() {
		return linkedWWidgets;
	}

	public void setLinkedWWidgets(List<WWidget> linkedWWidgets) {
		this.linkedWWidgets = linkedWWidgets;
	}

	public void add(WWidget... WWidgets) {
		for (WWidget WWidget : WWidgets) {
			if (!this.getLinkedWidgets().contains(WWidget)) {
				WWidget.setLinkedPanel(this);
				getLinkedWidgets().add(WWidget);
			}
		}
	}

	public void remove(WWidget... WWidgets) {
		for (WWidget WWidget : WWidgets) {
			if (this.getLinkedWidgets().contains(WWidget)) {
				getLinkedWidgets().remove(WWidget);
			}
		}
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (getFocus()) {
			switch (keyPressed) {
				case GLFW.GLFW_KEY_KP_SUBTRACT:
					setPositionX((int) Math.round(getPositionX() + 1));
					break;
				case GLFW.GLFW_KEY_KP_DIVIDE:
					setPositionX((int) Math.round(getPositionX() - 1));
					break;
				case GLFW.GLFW_KEY_KP_EQUAL:
					setPositionY((int) Math.round(getPositionY() + 1));
					break;
				case GLFW.GLFW_KEY_KP_8:
					setPositionY((int) Math.round(getPositionY() - 1));
					break;
			}
		}
		super.onKeyPressed(keyPressed, character, keyModifier);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (getCanMove() && getFocus() && mouseButton == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			setPositionX(getPositionX() + dragOffsetX);
			setPositionY(getPositionY() + dragOffsetY);
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void setTheme(String theme) {
		super.setTheme(theme);
		drawTheme = ResourceRegistry.get(getTheme()).getWPanelTheme();
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), drawTheme.getShadow(), drawTheme.getBackground(), drawTheme.getHighlight(), drawTheme.getOutline());

		if (hasLabel()) {
			BaseRenderer.getTextRenderer().drawWithShadow(getLabel(), (int) (getPositionX() + getSizeX() / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel()) / 2), (int) (positionY + 6), drawTheme.getLabel().RGB);
			BaseRenderer.drawRectangle(positionX, positionY + 16, positionZ, sizeX, 1, drawTheme.getOutline());
			BaseRenderer.drawRectangle(positionX + 1, positionY + 17, positionZ, sizeX - 2, 0.75, drawTheme.getShadow());
		}

		for (WWidget widget : getLinkedWidgets()) {
			widget.drawWidget();
		}
	}

	@Override
	public void tick() {
		for (WWidget widget : getLinkedWidgets()) {
			widget.tick();
		}
	}
}
