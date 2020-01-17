package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WHorizontalSlider extends WWidget {
	protected double limit = 0;
	protected double position = 0;
	protected String total;
	protected int tX;
	WHorizontalSlider.Theme drawTheme;
	public WHorizontalSlider(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int limit, WPanel linkedWPanel) {
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

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
		total = Integer.toString((int) Math.round(getPosition()));
		tX = (int) (getPositionX() + (getSizeX() + 7) / 2 - BaseRenderer.getTextRenderer().getStringWidth(Integer.toString((int) getPosition())) / 2);
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
	public void setTheme(String theme) {
		drawTheme = ResourceRegistry.get(getTheme()).getWHorizontalSliderTheme();
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

		BaseRenderer.getTextRenderer().drawWithShadow(total, tX, (int) (y + sY) + 4, drawTheme.getText().RGB);

		BaseRenderer.drawRectangle(x, y, z, (sX + 7), 1, drawTheme.getTopLeftBackground());
		BaseRenderer.drawRectangle(x, y, z, 1, sY, drawTheme.getTopLeftBackground());

		BaseRenderer.drawRectangle(x, y + sY, z, (sX + 7), 1, drawTheme.getBottomRightBackground());
		BaseRenderer.drawRectangle(x + (sX + 7), y, z, 1, sY + 1, drawTheme.getBottomRightBackground());

		BaseRenderer.drawRectangle(x + 1, y + 1, z, ((sX + 7) / l) * p - 1, sY - 1, drawTheme.getBackgroundOn());
		BaseRenderer.drawRectangle(x + ((sX + 7) / l) * p, y + 1, z, (sX + 7) - ((sX + 7) / l) * p, sY - 1, drawTheme.getBackgroundOff());

		BaseRenderer.drawBeveledPanel(x + (sX / l) * p, y - 1, z, 8, sY + 3, drawTheme.getTopLeftForeground(), drawTheme.getForeground(), drawTheme.getBottomRightForeground());
	}

	public class Theme extends WWidget.Theme {
		transient private WColor topLeftBackground;
		transient private WColor bottomRightBackground;
		transient private WColor backgroundOn;
		transient private WColor backgroundOff;
		transient private WColor topLeftForeground;
		transient private WColor bottomRightForeground;
		transient private WColor foreground;
		transient private WColor text;

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

		@SerializedName("text")
		private String rawText;

		public void build() {
			topLeftBackground = new WColor(rawTopLeftBackground);
			bottomRightBackground = new WColor(rawBottomRightBackground);
			backgroundOn = new WColor(rawBackgroundOn);
			backgroundOff = new WColor(rawBackgroundOff);
			topLeftForeground = new WColor(rawTopLeftForeground);
			bottomRightForeground = new WColor(rawBottomRightForeground);
			foreground = new WColor(rawForeground);
			text = new WColor(rawText);
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

		public WColor getText() {
			return text;
		}
	}
}
