package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import org.lwjgl.glfw.GLFW;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

import java.util.Optional;

public class WVerticalSlider extends WWidget {
	public class Theme {
		@SerializedName("top_left")
		private String topLeft;

		@SerializedName("bottom_right")
		private String bottomRight;

		@SerializedName("background")
		private String background;

		@SerializedName("foreground")
		private String foreground;

		public String getTopLeft() {
			return topLeft;
		}

		public void setTopLeft(String topLeft) {
			this.topLeft = topLeft;
		}

		public String getBottomRight() {
			return bottomRight;
		}

		public void setBottomRight(String bottomRight) {
			this.bottomRight = bottomRight;
		}

		public String getBackground() {
			return background;
		}

		public void setBackground(String background) {
			this.background = background;
		}

		public String getForeground() {
			return foreground;
		}

		public void setForeground(String foreground) {
			this.foreground = foreground;
		}
	}

	protected double limit = 0;
	protected double position = 0;

	protected String slidTotal;
	protected int slidStringPosition;

	public WVerticalSlider(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int limit, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLimit(limit);
	}

	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public void setSlidTotal(String slidTotal) {
		this.slidTotal = slidTotal;
	}

	public String getSlidTotal() {
		return slidTotal;
	}

	public void setSlidStringPosition(int slidStringPosition) {
		this.slidStringPosition = slidStringPosition;
	}

	public int getSlidStringPosition() {
		return slidStringPosition;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
		setSlidTotal(Integer.toString((int) Math.round(getPosition())));
		setSlidStringPosition((int) (getPositionY() + getSizeY() / 2 - BaseRenderer.getTextRenderer().getStringWidth(Integer.toString((int) getPosition())) / 2));
	}

	public void updatePosition(double mouseX, double mouseY) {
		if (scanFocus(mouseX, mouseY)) {
			setPosition((mouseY - getPositionY()) * (getLimit() / (getSizeY())));
		}
	}

	@Override
	public void onKeyPressed(int keyPressed) {
		if (getFocus() && keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT) {
			setPosition(Math.min(getPosition() + 1, getLimit() - 1));
		}
		if (getFocus() && keyPressed == GLFW.GLFW_KEY_KP_DIVIDE) {
			setPosition(getPosition() - 1 >= 0 ? getPosition() - 1 : 0);
		}
		super.onKeyPressed(keyPressed);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		updatePosition(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		updatePosition(mouseX, mouseY);
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public boolean isWithinBounds(double positionX, double positionY) {
		return (positionX >= getPositionX()
			 && positionX <= getPositionX() + getSizeX()
			 && positionY >= getPositionY()
			 && positionY <= getPositionY() + getSizeY() - 7);
	}

	@Override
	public boolean scanFocus(double mouseX, double mouseY) {
		Optional<? extends WWidget> isBelowOtherWidget = linkedWPanel.getLinkedWidgets().stream().filter((widget) ->
			   widget.getPositionZ() > getPositionZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		return setFocus(!isBelowOtherWidget.isPresent() && isWithinBounds(mouseX, mouseY));
	}

	@Override
	public void drawWidget() {
		WHorizontalSlider.Theme drawTheme = ResourceRegistry.get(getTheme()).getWHorizontalSliderTheme();

		BaseRenderer.getTextRenderer().draw(getSlidTotal(), (int) (getPositionX() + getSizeX() + 4), (int) (getPositionY() + getSizeY() / 2), 16);

		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), 1, drawTheme.getTopLeft());
		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), 1, getSizeY(), drawTheme.getTopLeft());

		BaseRenderer.drawRectangle(getPositionX(), getPositionY() + getSizeY() - 1, getPositionZ(), getSizeX(), 1, drawTheme.getBottomRight());
		BaseRenderer.drawRectangle(getPositionX() + getSizeX(), getPositionY(), getPositionZ(), 1, getSizeY(), drawTheme.getBottomRight());

		BaseRenderer.drawRectangle(getPositionX() + 1, getPositionY() + 1, getPositionZ(), getSizeX() - 1, (getSizeY() / getLimit()) * getPosition() - 1, drawTheme.getForeground());
		BaseRenderer.drawRectangle(getPositionX() + 1, getPositionY() + (getSizeY() / getLimit()) * getPosition() - 1, getPositionZ(), getSizeX() - 1, getSizeY() - (getSizeY() / getLimit()) * getPosition(), drawTheme.getBackground());

		BaseRenderer.drawBeveledPanel(getPositionX() - 1, getPositionY() + (getSizeY() / getLimit()) * getPosition() - 1, getPositionZ(), getSizeX() + 3, 8, drawTheme.getBottomRight(), drawTheme.getBackground(), drawTheme.getTopLeft());
	}
}
