package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import org.lwjgl.glfw.GLFW;
import spinnery.registry.ResourceRegistry;

import java.util.Optional;

public class WHorizontalSlider extends WWidget {
	public class Theme extends WWidget.Theme {
		transient private WColor topLeftBackground;
		transient private WColor bottomRightBackground;
		transient private WColor backgroundOn;
		transient private WColor backgroundOff;
		transient private WColor topLeftForeground;
		transient private WColor bottomRightForeground;
		transient private WColor foreground;

		@SerializedName("top_left_background")
		private String rawTopLeftBackground;

		@SerializedName("bottom_right_background")
		private String rawBottomRightBackground;

		@SerializedName("background_on")
		private String rawBackgroundOn;

		@SerializedName("background_off")
		private String rawBackgroundOff;

		@SerializedName("top_left_foreground")
		private String rawTopLeftForeground;

		@SerializedName("bottom_right_foreground")
		private String rawBottomRightForeground;

		@SerializedName("foreground")
		private String rawForeground;

		public void build() {
			topLeftBackground = new WColor(rawTopLeftBackground);
			bottomRightBackground = new WColor(rawBottomRightBackground);
			backgroundOn = new WColor(rawBackgroundOn);
			backgroundOff = new WColor(rawBackgroundOff);
			topLeftForeground = new WColor(rawTopLeftForeground);
			bottomRightForeground = new WColor(rawBottomRightForeground);
			foreground = new WColor(rawForeground);
		}

		public WColor getTopLeftBackground() {
			return topLeftBackground;
		}

		public WColor getBottomRightBackground() {
			return bottomRightBackground;
		}

		public WColor getBackgroundOn() {
			return backgroundOn;
		}

		public WColor getBackgroundOff() {
			return backgroundOff;
		}

		public WColor getTopLeftForeground() {
			return topLeftForeground;
		}

		public WColor getBottomRightForeground() {
			return bottomRightForeground;
		}

		public WColor getForeground() {
			return foreground;
		}
	}

	protected double limit = 0;
	protected double position = 0;

	protected String slidTotal;
	protected int slidStringPosition;

	public WHorizontalSlider(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int limit, WPanel linkedWPanel) {
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
		setSlidStringPosition((int) (getPositionX() + (getSizeX() + 7) / 2 - BaseRenderer.getTextRenderer().getStringWidth(Integer.toString((int) getPosition())) / 2));
	}

	public void updatePosition(double mouseX, double mouseY) {
		if (scanFocus(mouseX, mouseY)) {
			setPosition((mouseX - getPositionX()) * (getLimit() / (getSizeX())));
		}
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (getFocus() && keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT) {
			setPosition(Math.min(getPosition() + 1, getLimit() - 1));
		}
		if (getFocus() && keyPressed == GLFW.GLFW_KEY_KP_DIVIDE) {
			setPosition(getPosition() - 1 >= 0 ? getPosition() - 1 : 0);
		}
		super.onKeyPressed(keyPressed, character, keyModifier);
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
	public void drawWidget() {
		WHorizontalSlider.Theme drawTheme = ResourceRegistry.get(getTheme()).getWHorizontalSliderTheme();

		BaseRenderer.getTextRenderer().draw(getSlidTotal(), getSlidStringPosition(), (int) (getPositionY() + getSizeY()) + 4, 16);

		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), (getSizeX() + 7), 1, drawTheme.getTopLeftBackground());
		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), 1, getSizeY(), drawTheme.getTopLeftBackground());

		BaseRenderer.drawRectangle(getPositionX(), getPositionY() + getSizeY(), getPositionZ(), (getSizeX() + 7), 1, drawTheme.getBottomRightBackground());
		BaseRenderer.drawRectangle(getPositionX() + (getSizeX() + 7), getPositionY(), getPositionZ(), 1, getSizeY() + 1, drawTheme.getBottomRightBackground());

		BaseRenderer.drawRectangle(getPositionX() + 1, getPositionY() + 1, getPositionZ(), ((getSizeX() + 7) / getLimit()) * getPosition() - 1, getSizeY() - 1, drawTheme.getBackgroundOn());
		BaseRenderer.drawRectangle(getPositionX() + ((getSizeX() + 7) / getLimit()) * getPosition(), getPositionY() + 1, getPositionZ(), (getSizeX() + 7) - ((getSizeX() + 7) / getLimit()) * getPosition(), getSizeY() - 1, drawTheme.getBackgroundOff());

		BaseRenderer.drawBeveledPanel(getPositionX() + (getSizeX() / getLimit()) * getPosition(), getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, drawTheme.getTopLeftForeground(), drawTheme.getForeground(), drawTheme.getBottomRightForeground());
	}
}
