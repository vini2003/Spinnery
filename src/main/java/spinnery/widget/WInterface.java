package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import spinnery.common.BaseContainer;
import spinnery.registry.NetworkRegistry;
import spinnery.widget.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WInterface implements WModifiableCollection, WLayoutElement, WThemable {
	protected BaseContainer linkedContainer;
	protected Set<WWidget> heldWidgets = new LinkedHashSet<>();
	protected Map<Object, WWidget> cachedWidgets = new HashMap<>();
	protected boolean isClientside;
	protected Identifier theme;

	public void setClientside(Boolean clientside) {
		isClientside = clientside;
	}

	public WInterface() {
		setClientside(true);
	}

	public WInterface(BaseContainer linkedContainer) {
		setContainer(linkedContainer);
		if (getContainer().getLinkedWorld().isClient()) {
			setClientside(true);
		}
	}

	public boolean isClient() {
		return isClientside;
	}

	public BaseContainer getContainer() {
		return linkedContainer;
	}

	public void setContainer(BaseContainer linkedContainer) {
		this.linkedContainer = linkedContainer;
	}

	@Override
	public Identifier getTheme() {
		return theme;
	}

	public void setTheme(Identifier theme) {
		this.theme = theme;
	}

	public boolean isServer() {
		return !isClientside;
	}

	@Override
	public Set<WWidget> getWidgets() {
		return heldWidgets;
	}

	@Override
	public void add(WWidget... widgets) {
		heldWidgets.addAll(Arrays.asList(widgets));
	}

	@Override
	public void remove(WWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
	}

	@Override
	public boolean contains(WWidget... widgets) {
		return heldWidgets.containsAll(Arrays.asList(widgets));
	}

	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (WWidget widget : getAllWidgets()) {
			if (widget.getClass().isAnnotationPresent(WFocusedMouseListener.class) && !widget.getFocus())
				continue;
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseClickPacket(((WNetworked) widget), mouseX, mouseY, mouseButton));
			}
		}
	}

	public boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (WWidget widget : getAllWidgets()) {
			if (widget.getClass().isAnnotationPresent(WFocusedMouseListener.class) && !widget.getFocus())
				continue;
			widget.onMouseReleased(mouseX, mouseY, mouseButton);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseReleasePacket(((WNetworked) widget), mouseX, mouseY, mouseButton));
			}
		}
		return false;
	}

	public boolean onMouseDragged(int mouseX, int mouseY, int mouseButton, int deltaX, int deltaY) {
		for (WWidget widget : getAllWidgets()) {
			if (widget.getClass().isAnnotationPresent(WFocusedMouseListener.class) && !widget.getFocus())
				continue;
			widget.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseDragPacket(((WNetworked) widget), mouseX, mouseY, mouseButton, deltaX, deltaY));
			}
		}
		return false;
	}

	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		for (WWidget widget : getAllWidgets()) {
			if (widget.getClass().isAnnotationPresent(WFocusedMouseListener.class) && !widget.getFocus())
				continue;
			widget.onMouseScrolled(mouseX, mouseY, deltaY);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseScrollPacket(((WNetworked) widget), mouseX, mouseY, deltaY));
			}
		}
	}

	public void onMouseMoved(int mouseX, int mouseY) {
		for (WWidget widget : getAllWidgets()) {
			widget.updateFocus(mouseX, mouseY);
			if (widget.getClass().isAnnotationPresent(WFocusedMouseListener.class) && !widget.getFocus())
				continue;
			widget.onMouseMoved(mouseX, mouseY);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createFocusPacket(((WNetworked) widget), widget.getFocus()));
			}
		}
	}

	public void onKeyReleased(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getAllWidgets()) {
			if (widget.getClass().isAnnotationPresent(WFocusedKeyboardListener.class) && !widget.getFocus())
				continue;
			widget.onKeyReleased(keyCode);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createKeyReleasePacket(((WNetworked) widget), character, keyCode, keyModifier));
			}
		}
	}

	public void onKeyPressed(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getAllWidgets()) {
			if (widget.getClass().isAnnotationPresent(WFocusedKeyboardListener.class) && !widget.getFocus())
				continue;
			widget.onKeyPressed(character, keyCode, keyModifier);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createKeyPressPacket(((WNetworked) widget), character, keyCode, keyModifier));
			}
		}
	}

	public void onCharTyped(char character, int keyCode) {
		for (WWidget widget : getAllWidgets()) {
			if (widget.getClass().isAnnotationPresent(WFocusedKeyboardListener.class) && !widget.getFocus())
				continue;
			widget.onCharTyped(character);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createCharTypePacket(((WNetworked) widget), character, keyCode));
			}
		}
	}

	public void onDrawMouseoverTooltip(int mouseX, int mouseY) {
		for (WWidget widget : getAllWidgets()) {
			widget.onDrawTooltip();
		}
	}

	public void onAlign() {
		for (WWidget widget : getAllWidgets()) {
			widget.align();
			widget.onAlign();
		}
	}

	public void tick() {
		for (WWidget widget : getAllWidgets()) {
			widget.tick();
		}
	}

	@Override
	public void draw() {
		List<WWidget> widgets = new ArrayList<>(getWidgets());
		Collections.sort(widgets);

		for (WWidget widget : widgets) {
			widget.draw();
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getX() {
		return 0;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getY() {
		return 0;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getZ() {
		return 0;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getWidth() {
		return MinecraftClient.getInstance().getWindow().getScaledWidth();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getHeight() {
		return MinecraftClient.getInstance().getWindow().getScaledHeight();
	}
}
