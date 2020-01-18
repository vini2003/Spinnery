package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WDynamicText extends WWidget {
	protected boolean isSelected;
	protected String text = "";
	protected String clip = "";
	protected String visible = "";
	protected int selLeftPos = 0;
	protected int selRightPos = 0;
	protected int cursorPos;
	protected WDynamicText.Theme drawTheme;
	protected int offsetPos = 0;
	protected int fooY = 0;

	protected boolean isEditable;

	public WDynamicText(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedPanel) {
		setLinkedPanel(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");
	}

	void recalculateVisible() {
		if (MinecraftClient.getInstance().textRenderer.getStringWidth(text) > getSizeX()) {
			int h;
			for (h = 0; h < text.length() && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(0, h)) < sizeX - 12; ++ h)
				;
			fooY = h;

			int i;
			if (cursorPos > fooY) {
				for (i = cursorPos; i > 0 && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(i, cursorPos)) < sizeX - 12; -- i)
					;
				offsetPos = i;
				visible = text.substring(i, cursorPos);
			} else {
				visible = text.substring(0, h);
			}
		} else {
			offsetPos = 0;
			visible = text;
		}
	}

	void clearSelection() {
		selRightPos = - 1;
		selLeftPos = - 1;
	}

	boolean hasSelection() {
		return selRightPos != - 1 && selLeftPos != - 1;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean editable) {
		isEditable = editable;
	}

	@Override
	public void onCharTyped(char character) {
		if (! isSelected) {
			return;
		}

		text = new StringBuilder(text).insert(cursorPos, character).toString();
		++ cursorPos;

		clearSelection();
		recalculateVisible();

		super.onCharTyped(character);
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (! isSelected) {
			return;
		}

		long handle = MinecraftClient.getInstance().getWindow().getHandle();

		if (keyPressed == 30 && Screen.hasControlDown()) { // Ctrl w. A
			selLeftPos = 0;
			selRightPos = text.length() - 1;
			recalculateVisible();
		} else if (keyPressed == 32 && Screen.hasControlDown()) { // Ctrl w. D
			clearSelection();
			clip = "";
			recalculateVisible();
		} else if (keyPressed == 46 && Screen.hasControlDown()) { // Ctrl w. C
			if (selLeftPos >= 0 && selRightPos >= 0 && selRightPos <= text.length()) {
				clip = text.substring(selLeftPos, selRightPos);
				clearSelection();
			}
		} else if (keyPressed == 47 && Screen.hasControlDown()) { // Ctrl w. V
			text = new StringBuilder(text).insert(cursorPos, clip).toString();
			cursorPos += clip.length();
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && Screen.hasShiftDown() && cursorPos < text.length()) { // Right w. Shift
			selLeftPos = selLeftPos == - 1 ? cursorPos : selLeftPos;
			++ cursorPos;
			selRightPos = cursorPos;
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && Screen.hasShiftDown() && cursorPos > 0) { // Left w. Shift
			selRightPos = selRightPos == - 1 ? cursorPos : selRightPos;
			-- cursorPos;
			selLeftPos = cursorPos;
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && cursorPos <= text.length() - 1) { // Right
			++ cursorPos;
			clearSelection();
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && cursorPos > 0) { // Left
			-- cursorPos;
			clearSelection();
			recalculateVisible();
		} else if (keyPressed == 14 && cursorPos > 0 && text.length() > 0) { // Backspace
			-- cursorPos;
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos, text.length() - 1);
				cursorPos = selLeftPos;
				clearSelection();
			} else {
				text = new StringBuilder(text).deleteCharAt(cursorPos).toString();
			}
			recalculateVisible();
		} else if (keyPressed == 339 && cursorPos < text.length()) { // Delete
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos, text.length() - 1);
				cursorPos = selLeftPos;
				clearSelection();
			} else {
				text = new StringBuilder(text).deleteCharAt(cursorPos).toString();
			}
			recalculateVisible();
		}

		super.onKeyPressed(keyPressed, character, keyModifier);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (isEditable) {
			this.isSelected = scanFocus(mouseX, mouseY);
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void setTheme(String theme) {
		super.setTheme(theme);
		drawTheme = ResourceRegistry.get(getTheme()).getWDynamicTextTheme();
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

		BaseRenderer.drawBeveledPanel(x, y, z, sX, sY, drawTheme.getTopLeft(), drawTheme.getBackground(), drawTheme.getBottomRight());

		if (text.isEmpty() && ! isSelected) {
			BaseRenderer.getTextRenderer().drawWithShadow(getLabel(), (float) (positionX + 4), (float) (positionY + sizeY - 10), drawTheme.getLabel().RGB);
		} else {
			double pP = x + 4, pC = offsetPos;
			for (char c : visible.toCharArray()) {
				double cW = BaseRenderer.getTextRenderer().getCharWidth(c);
				BaseRenderer.getTextRenderer().drawWithShadow(String.valueOf(c), (float) pP, (float) (y + sY - 10), drawTheme.getText().RGB);

				if (pC >= selLeftPos && (pC < selRightPos || pC == text.length() - 1 && pC <= selRightPos) && (selLeftPos - selRightPos != 0)) {
					BaseRenderer.drawRectangle(pP, y + sY - 12, 10, cW, 12, drawTheme.getHighlight());
				}

				pP += cW;

				if ((pC == cursorPos || pC == cursorPos - 1) && isSelected) {
					BaseRenderer.getTextRenderer().drawWithShadow("|", (float) pP, (float) y + (float) sY - 10, drawTheme.getCursor().RGB);
				}

				++ pC;
			}
		}
	}

	public class Theme extends WWidget.Theme {
		transient private WColor topLeft;
		transient private WColor bottomRight;
		transient private WColor background;
		transient private WColor text;
		transient private WColor highlight;
		transient private WColor cursor;
		transient private WColor label;

		@SerializedName("top_left")
		private String rawTopLeft;

		@SerializedName("bottom_right")
		private String rawBottomRight;

		@SerializedName("background")
		private String rawBackground;

		@SerializedName("text")
		private String rawText;

		@SerializedName("highlight")
		private String rawHighlight;

		@SerializedName("cursor")
		private String rawCursor;

		@SerializedName("label")
		private String rawLabel;

		public void build() {
			topLeft = new WColor(rawTopLeft);
			bottomRight = new WColor(rawBottomRight);
			background = new WColor(rawBackground);
			text = new WColor(rawText);
			highlight = new WColor(rawHighlight);
			cursor = new WColor(rawCursor);
			label = new WColor(rawLabel);
		}

		public WColor getTopLeft() {
			return topLeft;
		}

		public WColor getBottomRight() {
			return bottomRight;
		}

		public WColor getBackground() {
			return background;
		}

		public WColor getText() {
			return text;
		}

		public WColor getHighlight() {
			return highlight;
		}

		public WColor getCursor() {
			return cursor;
		}

		public WColor getLabel() {
			return label;
		}
	}
}
