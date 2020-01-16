package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WDynamicText extends WWidget {
	public class Theme {
		@SerializedName("top_left")
		private String topLeft;

		@SerializedName("bottom_right")
		private String bottomRight;

		@SerializedName("background")
		private String background;

		@SerializedName("text")
		private String text;

		@SerializedName("highlight")
		private String highlight;

		@SerializedName("cursor")
		private String cursor;

		public String getTopLeft() {
			return topLeft;
		}

		public String getBottomRight() {
			return bottomRight;
		}

		public String getBackground() {
			return background;
		}

		public String getText() {
			return text;
		}

		public String getHighlight() {
			return highlight;
		}

		public String getCursor() {
			return cursor;
		}
	}

	protected boolean isSelected;

	protected String text = "";
	protected String clip = "";
	protected String visible = "";

	protected int selLeftPos = 0;
	protected int selRightPos = 0;
	protected int cursorPos;

	int offsetPos = 0;
	int fooY = 0;

	public WDynamicText(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		this.isSelected = scanFocus(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	void recalculateVisible() {
		if (MinecraftClient.getInstance().textRenderer.getStringWidth(text) > getSizeX()) {
			int h;
			for (h = 0; h < text.length() && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(0, h)) < sizeX - 12; ++h);
			fooY = h;

			int i;
			if (cursorPos > fooY) {
				for (i = cursorPos; i > 0 && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(i, cursorPos)) < sizeX - 12; --i);
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

		text = new StringBuilder(text).insert(cursorPos, character).toString();
		++cursorPos;

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
			text = new StringBuilder(text).insert(cursorPos, clip).toString();
			cursorPos += clip.length();
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) && cursorPos < text.length()) { // Right w. Shift
			selLeftPos = selLeftPos == -1 ? cursorPos : selLeftPos;
			++cursorPos;
			selRightPos = cursorPos;
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) && cursorPos > 0) { // Left w. Shift
			selRightPos = selRightPos == -1 ? cursorPos : selRightPos;
			--cursorPos;
			selLeftPos = cursorPos;
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && cursorPos <= text.length() - 1) { // Right
			++cursorPos;
			clearSelection();
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && cursorPos > 0) { // Left
			--cursorPos;
			clearSelection();
			recalculateVisible();
		} else if (keyPressed == 14 && cursorPos > 0 && text.length() > 0) { // Backspace
			--cursorPos;
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
	}


	@Override
	public void drawWidget() {
		WDynamicText.Theme drawTheme = ResourceRegistry.get(getTheme()).getWDynamicTextTheme();

		BaseRenderer.drawBeveledPanel(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), drawTheme.getTopLeft(), drawTheme.getBackground(), drawTheme.getBottomRight());

		double pP = positionX + 4, pC = offsetPos;
		for (char c : visible.toCharArray()) {
			double cW = BaseRenderer.getTextRenderer().getCharWidth(c);
			BaseRenderer.getTextRenderer().drawWithShadow(String.valueOf(c), (float) pP, (float) (positionY + sizeY - 10), Integer.decode(drawTheme.getText()));

			if (pC >= selLeftPos && (pC < selRightPos || pC == text.length() - 1 && pC <= selRightPos) && (selLeftPos - selRightPos != 0)) {
				BaseRenderer.drawRectangle(pP, getPositionY() + getSizeY() - 12, 10, cW, 12, drawTheme.getHighlight());
			}

			pP += cW;

			if (pC == cursorPos || pC == cursorPos - 1) {
				BaseRenderer.getTextRenderer().drawWithShadow("|", (float) pP , (float) positionY + (float) sizeY - 10, Integer.decode(drawTheme.getCursor()));
			}

			++pC;
		}
	}
}
