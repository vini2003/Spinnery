package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
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

	int selectedLeft = 0;
	int selectedRight = 0;

	int textPositionX = 0;

	int textPositionY = 0;

	int fooX = 0;
	int fooY = 0;

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
		return MinecraftClient.getInstance().textRenderer.getStringWidth(new StringBuilder(visible).substring(0, Math.max(position - fooX, 0)));
	}

	void recalculateVisible() {
		if (MinecraftClient.getInstance().textRenderer.getStringWidth(text) > getSizeX()) {
			int h=  0;
			for (h = 0; h < text.length() && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(0, h)) < sizeX - 12; ++h);
			fooY = h;

			int i = 0;
			if (position > fooY) {
				for (i = position; i > 0 && MinecraftClient.getInstance().textRenderer.getStringWidth(text.substring(i, position)) < sizeX - 12; --i);
				fooX = i;

				visible = text.substring(i, position);
			} else {
				visible = text.substring(0, h);
			}


			cursorX = getCursorX();
		} else {
			fooX = 0;

			visible = text;
			cursorX = getCursorX();
		}

	}

	@Override
	public void onCharTyped(char character) {
		if (!isSelected) {
			return;
		}

		text = new StringBuilder(text).insert(position, character).toString();
		++position;

		recalculateVisible();
	}

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (!isSelected) {
			return;
		}

		long handle = MinecraftClient.getInstance().window.getHandle();

		if (keyPressed == 30 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. A
			selectedLeft = 0;
			selectedRight = text.length() - 1;
		} else if (keyPressed == 32 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. D
			selectedLeft = -1;
			selectedRight = -1;
		} else if (keyPressed == 46 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. C
			clip = text.substring(selected[0], selected[1]);
		} else if (keyPressed == 47 && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) { // Ctrl w. V
			text = new StringBuilder(text).insert(position, clip).toString();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT)) { // Right Arrow w. Shift
			++position;
			selectedRight = position;
			selectedLeft = selectedLeft == -1 ? position : selectedLeft;
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) && position - 1 >= 0) { // Left Arrow w. Shift
			--position;
			selectedLeft = position;
			selectedRight = selectedRight == -1 ? position : selectedRight;
		} else if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT && position <= text.length() - 1) { // Right Arrow
			++position;
			recalculateVisible();
		} else if (keyPressed == GLFW.GLFW_KEY_KP_DIVIDE && position > 0) { // Left Arrow
			--position;
			recalculateVisible();
		} else if (keyPressed == 14 && position > 0 && text.length() > 0) { // Backspace
			--position;
			text = new StringBuilder(text).deleteCharAt(position).toString();
			recalculateVisible();
		} else if (keyPressed == 339 && position < text.length()) { // Delete
			text = new StringBuilder(text).deleteCharAt(position).toString();
			recalculateVisible();
		}
	}


	@Override
	public void drawWidget() {
		WDynamicText.Theme drawTheme = ResourceRegistry.get(getTheme()).getWDynamicTextTheme();

		BaseRenderer.drawRectangle(getPositionX() - 4, getPositionY(), getPositionZ() - 3, getSizeX() + 7 , 1, "0xff373737");
		BaseRenderer.drawRectangle(getPositionX() - 4, getPositionY() + 1, getPositionZ() - 3, 1, getSizeY(), "0xff373737");

		BaseRenderer.drawBeveledPanel(getPositionX() - 3, getPositionY() + 1, getPositionZ() - 3, getSizeX() + 7, getSizeY(), "0xffe0Ca9f", "0xffa09172", "0xff544c3b");

		BaseRenderer.drawRectangle(getPositionX() + getSizeX() + 4, getPositionY() + 1, getPositionZ() - 3, 1, getSizeY(), "0xffffffff");
		BaseRenderer.drawRectangle(getPositionX() - 3, getPositionY() + getSizeY() + 1, getPositionZ() - 3, getSizeX() + 8, 1, "0xffffffff");

		//GlStateManager.enableBlend();
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//int sizeA = MinecraftClient.getInstance().textRenderer.getStringWidth(rawText.getRawString().substring(Math.max(selected[0], 0), selected[1]));
		//int sizeB = MinecraftClient.getInstance().textRenderer.getStringWidth(rawText.getRawString().substring(0, Math.max(selected[0], 0)));

		//BaseRenderer.drawRectangle(getTextPositionX() + sizeB, getPositionY() + getSizeY() - 10, getPositionZ(), sizeA, 12, "0xA00000FF");

		//StringBuilder stringBuilder = new StringBuilder();
		//stringBuilder.append(rawText.getRawString(), 0, position);
		//BaseRenderer.drawRectangle(getTextPositionX() + MinecraftClient.getInstance().textRenderer.getStringWidth(stringBuilder.toString()) + 0.5, getPositionY() + getSizeY() - 12, getPositionZ(), 1.5, getSizeY() - 6, "0xff000000");

		BaseRenderer.getTextRenderer().drawWithShadow(visible, (float) positionX, (float) positionY + (float) sizeY - 10, Integer.decode("0xffffff"));
		BaseRenderer.getTextRenderer().drawWithShadow("|", (float) positionX + cursorX, (float) positionY + (float) sizeY - 10, Integer.decode("0xffffff"));
	}
}
