package spinnery.widget;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import spinnery.client.BaseRenderer;
import spinnery.registry.NetworkRegistry;
import spinnery.util.MouseUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WInterfaceHolder implements WModifiableCollection {
	List<WInterface> heldInterfaces = new ArrayList<>();

	public List<WInterface> getInterfaces() {
		return heldInterfaces;
	}

	@Override
	public boolean contains(WWidget... interfaces) {
		return heldInterfaces.containsAll(Arrays.asList(interfaces));
	}

	@Override
	public void add(WWidget... interfaces) {
		for (WWidget widget : interfaces) {
			if (!(widget instanceof WInterface)) continue;
			heldInterfaces.add((WInterface) widget);
		}
	}

	public void remove(WWidget... interfaces) {
		heldInterfaces.removeAll(Arrays.asList(interfaces));
	}

	public Set<WWidget> getWidgets() {
		Set<WWidget> widgets = new LinkedHashSet<>();

		for (WInterface myInterface : getInterfaces()) {
			widgets.addAll(myInterface.getWidgets());
		}

		return widgets;
	}

	public Set<WWidget> getAllWidgets() {
		Set<WWidget> widgets = new LinkedHashSet<>();
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgets.add(widgetA);
				if (widgetA instanceof WCollection) {
					widgets.addAll(((WCollection) widgetA).getAllWidgets());
				}
			}
		}
		return widgets;
	}

	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseClickPacket(((WSynced) widget), mouseX, mouseY, mouseButton));
			}
		}
		return false;
	}

	public boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseReleased(mouseX, mouseY, mouseButton);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseReleasePacket(((WSynced) widget), mouseX, mouseY, mouseButton));
			}
		}
		return false;
	}


	public boolean onMouseDragged(int mouseX, int mouseY, int mouseButton, int deltaX, int deltaY) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseDragPacket(((WSynced) widget), mouseX, mouseY, mouseButton, deltaX, deltaY));
			}
		}
		return false;
	}


	public boolean onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseScrolled(mouseX, mouseY, deltaY);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseScrollPacket(((WSynced) widget), mouseX, mouseY, deltaY));
			}
		}
		return false;
	}

	public void mouseMoved(int mouseX, int mouseY) {
		MouseUtilities.mouseX = mouseX;
		MouseUtilities.mouseY = mouseY;
		for (WWidget widget : getAllWidgets()) {
			widget.updateFocus(mouseX, mouseY);
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseMoved(mouseX, mouseY);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createFocusPacket(((WSynced) widget), widget.getFocus()));
			}
		}
	}


	public boolean onKeyReleased(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedKeyboardListener && !widget.getFocus()) continue;
			widget.onKeyReleased(keyCode);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createKeyReleasePacket(((WSynced) widget), character, keyCode, keyModifier));
			}
		}
		return false;
	}

	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedKeyboardListener && !widget.getFocus()) continue;
			widget.onKeyPressed(character, keyCode, keyModifier);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createKeyPressPacket(((WSynced) widget), character, keyCode, keyModifier));
			}
		}
		return false;
	}


	public boolean onCharTyped(char character, int keyCode) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedKeyboardListener && !widget.getFocus()) continue;
			widget.onCharTyped(character);
			if (widget instanceof WSynced) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createCharTypePacket(((WSynced) widget), character, keyCode));
			}
		}
		return false;
	}

	public void drawMouseoverTooltip(int mouseX, int mouseY) {
		for (WWidget widget : getAllWidgets()) {
			widget.onDrawTooltip();
		}
	}

	public void tick() {
		for (WWidget widget : getAllWidgets()) {
			widget.tick();
		}
	}

	public void draw() {
		Window window =  MinecraftClient.getInstance().getWindow();

		BaseRenderer.drawRectangle(0, 0, 0, window.getWidth(), window.getHeight(), WColor.of(0x90000000));

		for (WInterface myInterface : getInterfaces()) {
			myInterface.draw();
		}
	}
}
