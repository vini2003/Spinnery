package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.common.BaseContainer;
import spinnery.registry.ResourceRegistry;

import java.util.ArrayList;
import java.util.List;

public class WPanel extends WWidget {
	protected BaseContainer linkedContainer;
	protected List<WWidget> linkedWWidgets = new ArrayList<>();
	protected WPanel.Theme drawTheme;

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
			if (! this.getLinkedWidgets().contains(WWidget)) {
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
	public void draw() {
		if (isHidden()) {
			return;
		}

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

		for (WWidget widget : getLinkedWidgets()) {
			widget.draw();
		}
	}

	@Override
	public void tick() {
		for (WWidget widget : getLinkedWidgets()) {
			widget.tick();
		}
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
