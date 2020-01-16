package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WDynamicText extends WWidget {
	public class Theme {
		@SerializedName("text")
		private String textColor;

		@SerializedName("top_left_outermost")
		private String topLeftOutermost;

		@SerializedName("top_left_innermost")
		private String topLeftInnermost;

		@SerializedName("background")
		private String background;

		@SerializedName("bottom_right_outermost")
		private String bottomRightOutermost;

		@SerializedName("bottom_right_innermost")
		private String bottomRightInnermost;

		public String getTextColor() {
			return textColor;
		}

		public String getTopLeftOutermost() {
			return topLeftOutermost;
		}

		public String getTopLeftInnermost() {
			return topLeftInnermost;
		}

		public String getBackground() {
			return background;
		}

		public String getBottomRightOutermost() {
			return bottomRightOutermost;
		}

		public String getBottomRightInnermost() {
			return bottomRightInnermost;
		}
	}

	protected boolean isSelected;

	protected int position;

	protected int[] selected = new int[]{0, 0};

	protected String text = "", clip = "", visible = "";

	int cursorX = 0;

	int selLeftPos = 0;
	int selRightPos = 0;

	int textPositionX = 0;

	int textPositionY = 0;

	int offsetPos = 0;
	int fooY = 0;

	int i = 0, dSP = 0, dEP = 0;

	public WDynamicText(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		textPositionX = positionX;
		textPositionY = positionY;
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		this.isSelected = scanFocus(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	int getCursorX() {
		int offset = text.length() - visible.length();
		return MinecraftClient.getInstance().textRenderer.getStringWidth(new StringBuilder(visible).substring(0, Math.max(position - offsetPos, 0)));
	}

	void recalculateVisible() {
		if (MinecraftClient.getInstance().textRenderer.getStringWidth(text) > getSizeX()) {
			int h=  0;
			for (h = 0; h < text.length() && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(0, h)) < sizeX - 12; ++h);
			fooY = h;

			int i = 0;
			if (position > fooY) {
				for (i = position; i > 0 && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(i, position)) < sizeX - 12; --i);
				offsetPos = i;

				visible = text.substring(i, position);
			} else {
				visible = text.substring(0, h);
			}

			cursorX = getCursorX();
		} else {
			offsetPos = 0;

			visible = text;
			cursorX = getCursorX();
		}
	}

	void clearSelection() {
		selRightPos = -1;
		selLeftPos = -1;
	}

	boolean hasSelection() {
		return selRightPos != -1 && selLeftPos != -1;
	}

	void clearCopied() {
		clip = "";
	}

	@Override
	public void onCharTyped(char character) {
		if (!isSelected) {
			return;
		}

		text = new StringBuilder(text).insert(position, character).toString();
		++position;

		clearSelection();
		recalculateVisible();
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (!isSelected) {
			return;
		}

		long handle = MinecraftClient.getInstance().window.getHandle();

		if (keyPressed == 30 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. A
			selLeftPos = 0;
			selRightPos = text.length() - 1;
			recalculateVisible();
		} else if (keyPressed == 32 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. D
			clearSelection();
			clearCopied();
			recalculateVisible();
		} else if (keyPressed == 46 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. C
			if (selLeftPos >= 0 && selRightPos >= 0 && selRightPos <= text.length()) {
				clip = text.substring(selLeftPos, selRightPos);
				clearSelection();
			}
		} else if (keyPressed == 47 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. V
			text = new StringBuilder(text).insert(position, clip).toString();
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) && position < text.length()) { // Right Arrow w. Shift
			selLeftPos = selLeftPos == -1 ? position : selLeftPos;
			++position;
			selRightPos = position;
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) && position > 0) { // Left Arrow w. Shift
			selRightPos = selRightPos == -1 ? position : selRightPos;
			--position;
			selLeftPos = position;
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && position <= text.length() - 1) { // Right Arrow
			++position;
			clearSelection();
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && position > 0) { // Left Arrow
			--position;
			clearSelection();
			recalculateVisible();
		} else if (keyPressed == 14 && position > 0 && text.length() > 0) { // Backspace
			--position;
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos, text.length() - 1);
				position = selLeftPos;
				clearSelection();
			} else {
				text = new StringBuilder(text).deleteCharAt(position).toString();
			}
			recalculateVisible();
		} else if (keyPressed == 339 && position < text.length()) { // Delete
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos, text.length() - 1);
				position = selLeftPos;
				clearSelection();
			} else {
				text = new StringBuilder(text).deleteCharAt(position).toString();
			}
			recalculateVisible();
		}
	}


	@Override
	public void drawWidget() {
		WDynamicText.Theme drawTheme = ResourceRegistry.get(getTheme()).getWDynamicTextTheme();

		BaseRenderer.drawRectangle(getPositionX() - 4, getPositionY(), getPositionZ() - 3, getSizeX() + 7 , 1, "0xff373737");
		BaseRenderer.drawRectangle(getPositionX() - 4, getPositionY() + 1, getPositionZ() - 3, 1, getSizeY(), "0xff373737");

		BaseRenderer.drawBeveledPanel(getPositionX() - 3, getPositionY() + 1, getPositionZ() - 3, getSizeX() + 7, getSizeY(), "0xff000000", "0xff000000", "0xff000000");

		BaseRenderer.drawRectangle(getPositionX() + getSizeX() + 4, getPositionY() + 1, getPositionZ() - 3, 1, getSizeY(), "0xffffffff");
		BaseRenderer.drawRectangle(getPositionX() - 3, getPositionY() + getSizeY() + 1, getPositionZ() - 3, getSizeX() + 8, 1, "0xffffffff");

		BaseRenderer.drawRectangle(positionX + dSP, getPositionY() + getSizeY() - 12, getPositionZ(), dEP, 12, "0xB21407FF");

		double pP = positionX, pC = offsetPos;
		for (char c : visible.toCharArray()) {
			double cW = BaseRenderer.getTextRenderer().getCharWidth(c);
			BaseRenderer.getTextRenderer().drawWithShadow(String.valueOf(c), (float) pP, (float) (positionY + sizeY - 10), Integer.decode("0xffffff"));

			if (pC >= selLeftPos && (pC < selRightPos || pC == text.length() - 1 && pC <= selRightPos) && (selLeftPos - selRightPos != 0)) {
				BaseRenderer.drawRectangle(pP, getPositionY() + getSizeY() - 12, 10, cW, 12, "0xB30400FF");
			}

			if (pC == position || pC == position - 1) {
				BaseRenderer.getTextRenderer().drawWithShadow("|", (float) positionX + cursorX, (float) positionY + (float) sizeY - 10, Integer.decode("0xffffff"));
			}

			pP += cW;
			++pC;
		}
	}
}
