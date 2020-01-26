package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class WDynamicText extends WWidget implements WClient {
	public static final int TOP_LEFT = 0;
	public static final int BOTTOM_RIGHT = 1;
	public static final int BACKGROUND = 2;
	public static final int TEXT = 3;
	public static final int HIGHLIGHT = 4;
	public static final int CURSOR = 5;
	public static final int LABEL = 6;
	protected boolean isSelected;
	protected String text = "";
	protected String visible = "";
	protected int selLeftPos = 0;
	protected int selRightPos = 0;
	protected int cursorPos;
	protected int offsetPos = 0;
	protected int cursorTick = 0;
	protected boolean isEditable = true;

	public WDynamicText(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);


		setPosition(position);

		setSize(size);

		setTheme("light");
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(TOP_LEFT, WColor.of(rawTheme.get("top_left")));
		theme.add(BOTTOM_RIGHT, WColor.of(rawTheme.get("bottom_right")));
		theme.add(BACKGROUND, WColor.of(rawTheme.get("background")));
		theme.add(TEXT, WColor.of(rawTheme.get("text")));
		theme.add(HIGHLIGHT, WColor.of(rawTheme.get("highlight")));
		theme.add(CURSOR, WColor.of(rawTheme.get("cursor")));
		theme.add(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		updateText();
	}

	void updateText() {
		visible = "";
		int sW = 0;
		for (char c : text.toCharArray()) {
			visible += c;
			if (c != '\n') {
				sW += BaseRenderer.getTextRenderer().getCharWidth(c);
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

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean editable) {
		isEditable = editable;
	}

	@Override
	public void onCharTyped(char character) {
		if (!isSelected) {
			return;
		}

		text = new StringBuilder(text).insert(cursorPos, character).toString();
		++cursorPos;

		clearSelection();
		updateText();

		super.onCharTyped(character);
	}

	void clearSelection() {
		selRightPos = -1;
		selLeftPos = -1;
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (!isSelected) {
			return;
		}

		if (keyPressed == GLFW.GLFW_KEY_A && Screen.hasControlDown()) { // Ctrl w. A
			selLeftPos = 0;
			selRightPos = text.length() - 1;
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_D && Screen.hasControlDown()) { // Ctrl w. D
			clearSelection();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_C && Screen.hasControlDown()) { // Ctrl w. C
			if (selLeftPos >= 0 && selRightPos >= 0 && selRightPos <= text.length()) {
				MinecraftClient.getInstance().keyboard.setClipboard(text.substring(selLeftPos, selRightPos));
				clearSelection();
			}
		} else if (keyPressed == GLFW.GLFW_KEY_V && Screen.hasControlDown()) { // Ctrl w. V
			text = new StringBuilder(text).insert(cursorPos, MinecraftClient.getInstance().keyboard.getClipboard()).toString();
			cursorPos += MinecraftClient.getInstance().keyboard.getClipboard().length();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_RIGHT && Screen.hasShiftDown() && cursorPos < text.length()) { // Right w. Shift
			selLeftPos = selLeftPos == -1 ? cursorPos : selLeftPos;
			++cursorPos;
			selRightPos = cursorPos;
			updateText();

		} else if (keyPressed == GLFW.GLFW_KEY_LEFT && Screen.hasShiftDown() && cursorPos > 0) { // Left w. Shift
			selRightPos = selRightPos == -1 ? cursorPos : selRightPos;
			--cursorPos;
			selLeftPos = cursorPos;
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_RIGHT && cursorPos <= text.length() - 1) { // Right
			++cursorPos;
			clearSelection();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_LEFT && cursorPos > 0) { // Left
			--cursorPos;
			clearSelection();
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_BACKSPACE && cursorPos > 0 && text.length() > 0) { // Backspace
			--cursorPos;
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos, text.length() - 1);
				cursorPos = selLeftPos;
				clearSelection();
			} else {
				text = new StringBuilder(text).deleteCharAt(cursorPos).toString();
			}
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_DELETE && cursorPos < text.length()) { // Delete
			if (hasSelection()) {
				text = text.substring(0, selLeftPos) + text.substring(selRightPos, text.length() - 1);
				cursorPos = selLeftPos;
				clearSelection();
			} else {
				text = new StringBuilder(text).deleteCharAt(cursorPos).toString();
			}
			updateText();
		} else if (keyPressed == GLFW.GLFW_KEY_ENTER) {
			text += '\n';
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
			this.isSelected = updateFocus(mouseX, mouseY);
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

		int oY = 0;

		BaseRenderer.drawBeveledPanel(x, y, z, sX, sY, getColor(TOP_LEFT), getColor(BACKGROUND), getColor(BOTTOM_RIGHT));

		if (text.isEmpty() && !isSelected) {
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + 4, y + 4 + oY, getColor(LABEL).RGB);
		} else {
			int pP = x + 4, pC = offsetPos;
			if (visible.isEmpty() && cursorTick > 10) {
				BaseRenderer.drawText(isLabelShadowed(), "|", pP, y + 4 + oY, getColor(CURSOR).RGB);
			}
			for (char c : visible.toCharArray()) {
				if (c == '\n') {
					oY += 9;
					pP = x + 4;
				} else {
					int cW = (int) BaseRenderer.getTextRenderer().getCharWidth(c);
					BaseRenderer.drawText(isLabelShadowed(), String.valueOf(c), pP, y + 4 + oY, getColor(TEXT).RGB);

					if (pC >= selLeftPos && (pC < selRightPos || pC == text.length() - 1 && pC <= selRightPos) && (selLeftPos - selRightPos != 0)) {
						BaseRenderer.drawRectangle(pP, y + 4 + oY, 10, cW, 9, getColor(HIGHLIGHT));
					}

					pP += cW;

					if ((pC == cursorPos - 1) && isSelected && cursorTick > 10) {
						BaseRenderer.drawText(isLabelShadowed(), "|", pP, y + 4 + oY, getColor(CURSOR).RGB);
					}

					++pC;
				}
			}
		}
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

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}
}
