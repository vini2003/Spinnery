package spinnery.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;
import spinnery.widget.WInterface;

public class BaseScreen extends Screen {
	protected final WInterface screenInterface = new WInterface();

	private boolean isPauseScreen = false;

	public BaseScreen() {
		super(new LiteralText(""));
	}

	@Override
	public void render(int mouseX, int mouseY, float tick) {
		getScreenInterface().draw();
	}

	public void setIsPauseScreen(boolean isPauseScreen) {
		this.isPauseScreen = isPauseScreen;
	}

	public WInterface getScreenInterface() {
		return screenInterface;
	}

	@Override
	public boolean keyPressed(int keyCode, int character, int keyModifier) {
		screenInterface.onKeyPressed(keyCode, character, keyModifier);

		if (character == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.player.closeScreen();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void tick() {
		getScreenInterface().tick();
	}

	@Override
	public boolean isPauseScreen() {
		return isPauseScreen;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		screenInterface.onAlign();
		super.resize(client, width, height);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		screenInterface.onMouseClicked((int) mouseX, (int) mouseY, mouseButton);
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		screenInterface.onMouseReleased((int) mouseX, (int) mouseY, mouseButton);
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
		screenInterface.onMouseDragged((int) mouseX, (int) mouseY, mouseButton, (int) deltaX, (int) deltaY);
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
		screenInterface.onMouseScrolled((int) mouseX, (int) mouseY, deltaY);
		return false;
	}

	@Override
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		screenInterface.onKeyReleased(character, keyCode, keyModifier);
		return false;
	}

	@Override
	public boolean charTyped(char character, int keyCode) {
		screenInterface.onCharTyped(character, keyCode);
		return super.charTyped(character, keyCode);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		screenInterface.onMouseMoved((int) mouseX, (int) mouseY);
	}
}
