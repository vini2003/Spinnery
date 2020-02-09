package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import spinnery.common.BaseContainer;
import spinnery.registry.NetworkRegistry;
import spinnery.util.EventUtilities;
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
	protected Set<WAbstractWidget> heldWidgets = new LinkedHashSet<>();
	protected Map<Class<? extends WAbstractWidget>, WAbstractWidget> cachedWidgets = new HashMap<>();
	protected boolean isClientside;
	protected Identifier theme;

	public <W extends WInterface> W setClientside(Boolean clientside) {
		isClientside = clientside;
		return (W) this;
	}

	public WInterface() {
		setClientside(true);
	}

	public WInterface(BaseContainer linkedContainer) {
		setContainer(linkedContainer);
		if (getContainer().getWorld().isClient()) {
			setClientside(true);
		}
	}

	public boolean isClient() {
		return isClientside;
	}

	public Map<Class<? extends WAbstractWidget>, WAbstractWidget> getCachedWidgets() {
		return cachedWidgets;
	}

	public BaseContainer getContainer() {
		return linkedContainer;
	}

	public <W extends WInterface> W setContainer(BaseContainer linkedContainer) {
		this.linkedContainer = linkedContainer;
		return (W) this;
	}

	@Override
	public Identifier getTheme() {
		return theme;
	}

	public <W extends WInterface> W setTheme(Identifier theme) {
		this.theme = theme;
		return (W) this;
	}

	public <W extends WInterface> W setTheme(String theme) {
		return setTheme(new Identifier(theme));
	}

	public boolean isServer() {
		return !isClientside;
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return heldWidgets;
	}

	@Override
	public void add(WAbstractWidget... widgets) {
		heldWidgets.addAll(Arrays.asList(widgets));
	}

	@Override
	public void remove(WAbstractWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return heldWidgets.containsAll(Arrays.asList(widgets));
	}

	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (WAbstractWidget widget : getWidgets()) {
			if (!EventUtilities.canReceiveMouse(widget)) continue;
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseClickPacket(((WNetworked) widget), mouseX, mouseY, mouseButton));
			}
		}
	}

	public boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (WAbstractWidget widget : getWidgets()) {
			if (!EventUtilities.canReceiveMouse(widget)) continue;
			widget.onMouseReleased(mouseX, mouseY, mouseButton);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseReleasePacket(((WNetworked) widget), mouseX, mouseY, mouseButton));
			}
		}
		return false;
	}

	public boolean onMouseDragged(int mouseX, int mouseY, int mouseButton, int deltaX, int deltaY) {
		for (WAbstractWidget widget : getWidgets()) {
			if (!EventUtilities.canReceiveMouse(widget)) continue;
			widget.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseDragPacket(((WNetworked) widget), mouseX, mouseY, mouseButton, deltaX, deltaY));
			}
		}
		return false;
	}

	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		for (WAbstractWidget widget : getWidgets()) {
			if (!EventUtilities.canReceiveMouse(widget)) continue;
			widget.onMouseScrolled(mouseX, mouseY, deltaY);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createMouseScrollPacket(((WNetworked) widget), mouseX, mouseY, deltaY));
			}
		}
	}

	public void onMouseMoved(int mouseX, int mouseY) {
		for (WAbstractWidget widget : getWidgets()) {
			widget.updateFocus(mouseX, mouseY);
			if (!EventUtilities.canReceiveMouse(widget)) continue;
			widget.onMouseMoved(mouseX, mouseY);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createFocusPacket(((WNetworked) widget), widget.isFocused()));
			}
		}
	}

	public void onKeyReleased(int keyCode, int character, int keyModifier) {
		for (WAbstractWidget widget : getWidgets()) {
			if (!EventUtilities.canReceiveKeyboard(widget)) continue;
			widget.onKeyReleased(keyCode, character, keyModifier);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createKeyReleasePacket(((WNetworked) widget), character, keyCode, keyModifier));
			}
		}
	}

	public void onKeyPressed(int keyCode, int character, int keyModifier) {
		for (WAbstractWidget widget : getWidgets()) {
			if (!EventUtilities.canReceiveKeyboard(widget)) continue;
			widget.onKeyPressed(keyCode, character, keyModifier);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createKeyPressPacket(((WNetworked) widget), character, keyCode, keyModifier));
			}
		}
	}

	public void onCharTyped(char character, int keyCode) {
		for (WAbstractWidget widget : getWidgets()) {
			if (!EventUtilities.canReceiveKeyboard(widget)) continue;
			widget.onCharTyped(character, keyCode);
			if (widget instanceof WNetworked) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
						NetworkRegistry.createCharTypePacket(((WNetworked) widget), character, keyCode));
			}
		}
	}

	public void onDrawMouseoverTooltip(int mouseX, int mouseY) {
		for (WAbstractWidget widget : getWidgets()) {
			widget.onDrawTooltip(mouseX, mouseY);
		}
	}

	public void onAlign() {
		for (WAbstractWidget widget : getWidgets()) {
			widget.align();
			widget.onAlign();
		}
	}

	public void tick() {
		for (WAbstractWidget widget : getAllWidgets()) {
			widget.tick();
		}
	}

	@Override
	public void draw() {
		List<WAbstractWidget> widgets = new ArrayList<>(getWidgets());
		Collections.sort(widgets);

		for (WAbstractWidget widget : widgets) {
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
