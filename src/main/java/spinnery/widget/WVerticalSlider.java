package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WVerticalSlider extends WWidget {
	protected double limit = 0;
	protected double position = 0;
	protected String slidTotal;
	protected int slidStringPosition;
	WVerticalSlider.Theme drawTheme;
	public WVerticalSlider(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int limit, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");

		setLimit(limit);
	}

	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public String getSlidTotal() {
		return slidTotal;
	}

	public void setSlidTotal(String slidTotal) {
		this.slidTotal = slidTotal;
	}

	public int getSlidStringPosition() {
		return slidStringPosition;
	}

	public void setSlidStringPosition(int slidStringPosition) {
		this.slidStringPosition = slidStringPosition;
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
	public void setTheme(String theme) {
		super.setTheme(theme);
		drawTheme = ResourceRegistry.get(getTheme()).getWVerticalSliderTheme();
	}

	@Override
	public void draw() {
		double l = getLimit();
		double p = getPosition();

		double x = getPositionX();
		double y = getPositionY();
		double z = getPositionZ();

		double sX = getSizeX();
		double sY = getSizeY();

		BaseRenderer.getTextRenderer().drawWithShadow(getSlidTotal(), (int) (x + sX + 4), (int) (y + sY / 2), 0xffffff);

		BaseRenderer.drawRectangle(x, y, z, sX, 1, drawTheme.getTopLeftBackground());
		BaseRenderer.drawRectangle(x, y, z, 1, (sY + 7), drawTheme.getTopLeftBackground());

		BaseRenderer.drawRectangle(x, y + (sY + 7) - 1, z, sX, 1, drawTheme.getBottomRightBackground());
		BaseRenderer.drawRectangle(x + sX, y, z, 1, (sY + 7), drawTheme.getBottomRightBackground());

		BaseRenderer.drawRectangle(x + 1, y + 1, z, sX - 1, ((sY + 7) / l) * p - 2, drawTheme.getBackgroundOn());
		BaseRenderer.drawRectangle(x + 1, y + ((sY + 7) / l) * p - 1, z, sX - 1, (sY + 7) - ((sY + 7) / l) * p, drawTheme.getBackgroundOff());

		BaseRenderer.drawBeveledPanel(x - 1, y + (sY / l) * p - 1, z, sX + 3, 8, drawTheme.getTopLeftForeground(), drawTheme.getForeground(), drawTheme.getBottomRightForeground());
	}

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
}
