package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;

@Environment(EnvType.CLIENT)
public class WDynamicText extends WAbstractWidget {
	protected boolean isActive;
	protected String text = "";
	protected String visible = "";
	protected int selLeftPos = 0;
	protected int selRightPos = 0;
	protected int cursorPos;
	protected int offsetPos = 0;
	protected int cursorTick = 0;
	protected boolean isEditable = true;
	protected int currentLine = 0;

	public String getText() {
		return text;
	}

	public <W extends WDynamicText> W setText(String text) {
		this.text = text;
		updateText();
		return (W) this;
	}

	@Override
	public void onCharTyped(char character, int keyCode) {
		if (!isActive) {
			return;
		}

		if (hasSelection()) {
			text = "";
		}
		text = new StringBuilder(text).insert(cursorPos, character).toString();
		++cursorPos;

		clearSelection();
		updateText();

		super.onCharTyped(character, keyCode);
	}

	public boolean isEditable() {
		return isEditable;
	}

	public <W extends WDynamicText> W setEditable(boolean editable) {
		isEditable = editable;
		return (W) this;
	}

	void updateText() {
		visible = "";
		int sW = 0;
		for (char c : text.toCharArray()) {
			visible += c;
			if (c != '\n') {
				sW += TextRenderer.width(c);
				if (sW > getWidth() - 12) {
					visible += '\n';
					sW = 0;
				}
			} else {
				sW = 0;
			}
		}


		int offsetA = visible.length() - (int) (visible.chars().filter(c -> c == '\n').count());

		while (visible.chars().filter(c -> c == '\n').count() > (int) (Math.floor((getHeight() - 12) / 9))) {
			for (int i = 0; i < visible.length(); ++i) {
				if (visible.charAt(i) == '\n') {
					visible = visible.substring(i + 1);
					break;
				}
			}
		}

		int offsetB = visible.length();

		offsetPos = Math.max(offsetA - offsetB, 0);
	}

	void clearSelection() {
		selRightPos = -1;
		selLeftPos = -1;
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (!isActive) {
			return;
		}

		cursorTick = 20;
		if (keyPressed == GLFW.GLFW_KEY_A && Screen.hasControlDown()) { // Ctrl w. A
			selLeftPos = 0;
			selRightPos = text.length() - 1;
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_D && Screen.hasControlDown()) { // Ctrl w. D
			clearSelection();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_C && Screen.hasControlDown()) { // Ctrl w. C
			if (selLeftPos >= -1 && selRightPos >= 0 && selRightPos <= text.length()) {
				MinecraftClient.getInstance().keyboard.setClipboard(text.substring(selLeftPos, selRightPos));
				clearSelection();
			}
		} else if (keyPressed == GLFW.GLFW_KEY_X && Screen.hasControlDown()) { // Ctrl w. X
			if (selLeftPos >= -1 && selRightPos >= 0 && selRightPos <= text.length()) {
				MinecraftClient.getInstance().keyboard.setClipboard(text.substring(selLeftPos, selRightPos));
				text = text.substring(0, selLeftPos) + text.substring(selRightPos + 1);
				cursorPos = selLeftPos;
				clearSelection();
				updateText();
			}
		} else if (keyPressed == GLFW.GLFW_KEY_V && Screen.hasControlDown()) { // Ctrl w. V
			text = new StringBuilder(text).insert(cursorPos, MinecraftClient.getInstance().keyboard.getClipboard()).toString();
			cursorPos += MinecraftClient.getInstance().keyboard.getClipboard().length();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_RIGHT && Screen.hasShiftDown() && cursorPos < text.length()) { // Right w. Shift
			++cursorPos;
			if (hasSelection()) {
				if (cursorPos > selRightPos) {
					selRightPos = cursorPos;
				} else {
					selLeftPos = cursorPos;
				}
			} else {
				selLeftPos = selLeftPos == -1 ? cursorPos - 1 : selLeftPos;
				selRightPos = cursorPos;
			}
			updateText();

		} else if (keyPressed == GLFW.GLFW_KEY_LEFT && Screen.hasShiftDown() && cursorPos > 0) { // Left w. Shift
			--cursorPos;
			if (hasSelection()) {
				if (cursorPos >= selLeftPos) {
					selRightPos = cursorPos;
				} else {
					selLeftPos = cursorPos;
				}
			} else {
				selRightPos = selRightPos == -1 ? cursorPos + 1 : selRightPos;
				selLeftPos = cursorPos;
			}
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_RIGHT && cursorPos <= text.length() - 1) { // Right
			++cursorPos;
			clearSelection();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_LEFT && cursorPos > 0) { // Left
			--cursorPos;
			clearSelection();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_BACKSPACE && text.length() > 0) { // Backspace
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos);
				cursorPos = selLeftPos;
				clearSelection();
			} else {
				if (cursorPos > 0) {
					--cursorPos;
					text = new StringBuilder(text).deleteCharAt(cursorPos).toString();
				}
			}
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_DELETE && text.length() > 0) { // Delete
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos);
				cursorPos = selLeftPos;
				clearSelection();
			} else {
				if (cursorPos < text.length())
					text = new StringBuilder(text).deleteCharAt(cursorPos).toString();
			}
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_HOME) {
			if (Screen.hasShiftDown()) {
				if (hasSelection()) {
					if (cursorPos >= selRightPos) {
						selRightPos = selLeftPos;
					}
				} else {
					selRightPos = cursorPos;
				}
				selLeftPos = 0;
			} else {
				clearSelection();
			}
			cursorPos = 0;
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_END) {
			if (Screen.hasShiftDown()) {
				if (hasSelection()) {
					if (cursorPos <= selLeftPos) {
						selLeftPos = selRightPos;
					}
				} else {
					selLeftPos = cursorPos;
				}
				selRightPos = text.length();
			} else {
				clearSelection();
			}
			cursorPos = text.length();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_ENTER) {
			text = new StringBuilder(text).insert(cursorPos, '\n').toString();
			++cursorPos;
			updateText();
		}

		super.onKeyPressed(keyPressed, character, keyModifier);
	}

	boolean hasSelection() {
		return selRightPos != -1 && selLeftPos != -1;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (isEditable) {
			this.isActive = updateFocus(mouseX, mouseY);
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawBeveledPanel(x, y, z, sX, sY, getStyle().asColor("top_left"), getStyle().asColor("background"), getStyle().asColor("bottom_right"));

		if (text.isEmpty() && !isActive) {
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + 4, y + 4, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
		}

		int cH = TextRenderer.height();
		int oX = 0;
		int oY = 0;
		int right = x + sX - 8;
		int bottom = y + sY - 8;
		if (!text.isEmpty()) {
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				int cX = x + 4 + oX;
				int cY = y + 4 + oY;
				int cW = TextRenderer.width(c);
				if (c == '\n') {
					if (isSelected(i)) {
						BaseRenderer.drawRectangle(cX, cY, z, right - cX, cH, getStyle().asColor("highlight"));
					}
					oX = 0;
					oY += cH + 2;
					if (oY > y + sY - 8) {
						break;
					}
				} else {
					TextRenderer.pass().text(c).at(cX, cY, z).color(getStyle().asColor("text")).render();
					if (isSelected(i)) {
						BaseRenderer.drawRectangle(cX, cY, z, cW, cH, getStyle().asColor("highlight"));
					}
					if (cX + cW > right) {
						if (cY + cH + 2 > bottom) {
							break;
						}
						oX = 0;
						oY += cH + 2;
					} else {
						oX += cW;
					}
				}
				if (isActive && cursorPos == i + 1 && cursorTick > 10) {
					BaseRenderer.drawRectangle(x + 4 + oX, y + 4 + oY, z, 1, cH, getStyle().asColor("cursor"));
				}
			}
		}
		if (isActive && cursorPos == 0 && cursorTick > 10) {
			BaseRenderer.drawRectangle(x + 4, y + 4, z, 1, cH, getStyle().asColor("cursor"));
			currentLine = 0;
		}
	}

	protected boolean isSelected(int index) {
		return selLeftPos <= index && selRightPos > index;
	}

	@Override
	public void tick() {
		if (cursorTick > 0) {
			--cursorTick;
		} else {
			cursorTick = 20;
		}
		super.tick();
	}
}
