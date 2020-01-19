package spinnery.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;
import spinnery.widget.WCollection;
import spinnery.widget.WInterface;
import spinnery.widget.WWidget;

public class BaseScreen extends Screen {
	WInterface linkedInterface;
	private boolean isPauseScreen = false;

	public BaseScreen() {
		super(new LiteralText(""));
	}

	public WInterface getInterface() {
		return linkedInterface;
	}

	public void setInterface(WInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
	}

	public void setIsPauseScreen(boolean isPauseScreen)
	{
		this.isPauseScreen = isPauseScreen;
	}

	@Override
	public boolean isPauseScreen() {
		return isPauseScreen;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.scanFocus(mouseX, mouseY);
			widget.onMouseMoved(mouseX, mouseY);
		}

		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public void render(int mouseX, int mouseY, float tick) {
		getInterface().draw();

		super.render(mouseX, mouseY, tick);
	}

	@Override
	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.onKeyPressed(keyCode, character, keyModifier);
		}

		if (character == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.player.closeContainer();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void tick() {
		for (WWidget widgetA : getInterface().getWidgets()) {
			widgetA.tick();

			if (widgetA instanceof WCollection) {
				for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
					widgetB.tick();
				}
			}
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.onMouseReleased(mouseX, mouseY, mouseButton);
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseDragged(double slotX, double slotY, int mouseButton, double mouseX, double mouseY) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.onMouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
		}

		return super.mouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double mouseZ) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.onMouseScrolled(mouseX, mouseY, mouseZ);
		}

		return super.mouseScrolled(mouseX, mouseY, mouseZ);
	}

	@Override
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.onKeyReleased(keyCode);
		}
		return super.keyReleased(character, keyCode, keyModifier);
	}

	@Override
	public boolean charTyped(char character, int keyCode) {
		for (WWidget widget : getInterface().getWidgets()) {
			widget.onCharTyped(character);
		}

		return super.charTyped(character, keyCode);
	}
}
