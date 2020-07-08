package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;
import spinnery.common.registry.NetworkRegistry;
import spinnery.common.utility.EventUtilities;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WNetworked;
import spinnery.widget.api.WThemable;

import java.util.*;

public class WInterface implements WModifiableCollection, WLayoutElement, WThemable {
    protected BaseContainer linkedContainer;
    protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();
    protected Map<Class<? extends WAbstractWidget>, WAbstractWidget> cachedWidgets = new HashMap<>();
    protected boolean isClientside;
    protected Identifier theme;

    public WInterface() {
        setClientside(true);
    }

    public <W extends WInterface> W setClientside(Boolean clientside) {
        isClientside = clientside;
        return (W) this;
    }

    public WInterface(BaseContainer linkedContainer) {
        setContainer(linkedContainer);
        if (getContainer().getWorld().isClient()) {
            setClientside(true);
        }
    }

    public BaseContainer getContainer() {
        return linkedContainer;
    }

    public <W extends WInterface> W setContainer(BaseContainer linkedContainer) {
        this.linkedContainer = linkedContainer;
        return (W) this;
    }

    public boolean isClient() {
        return isClientside;
    }

    public Map<Class<? extends WAbstractWidget>, WAbstractWidget> getCachedWidgets() {
        return cachedWidgets;
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
    public void add(WAbstractWidget... widgets) {
        this.widgets.addAll(Arrays.asList(widgets));
        onLayoutChange();
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
        return widgets;
    }

    @Override
    public boolean contains(WAbstractWidget... widgets) {
        return this.widgets.containsAll(Arrays.asList(widgets));
    }

    @Override
    public void remove(WAbstractWidget... widgets) {
        this.widgets.removeAll(Arrays.asList(widgets));
        onLayoutChange();
    }

    public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
        for (WAbstractWidget widget : getWidgets()) {
            if (!EventUtilities.canReceiveMouse(widget)) continue;
            widget.onMouseClicked(mouseX, mouseY, mouseButton);
            if (widget instanceof WNetworked) {
                ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
                        NetworkRegistry.createMouseClickPacket(((WNetworked) widget), mouseX, mouseY, mouseButton));
            }
        }
    }

    public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
        for (WAbstractWidget widget : getWidgets()) {
            if (!EventUtilities.canReceiveMouse(widget)) continue;
            widget.onMouseReleased(mouseX, mouseY, mouseButton);
            if (widget instanceof WNetworked) {
                ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
                        NetworkRegistry.createMouseReleasePacket(((WNetworked) widget), mouseX, mouseY, mouseButton));
            }
        }
    }

    public boolean onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY) {
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

    public void onMouseScrolled(float mouseX, float mouseY, double deltaY) {
        for (WAbstractWidget widget : getWidgets()) {
            if (!EventUtilities.canReceiveMouse(widget)) continue;
            widget.onMouseScrolled(mouseX, mouseY, deltaY);
            if (widget instanceof WNetworked) {
                ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
                        NetworkRegistry.createMouseScrollPacket(((WNetworked) widget), mouseX, mouseY, deltaY));
            }
        }
    }

    public void onMouseMoved(float mouseX, float mouseY) {
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

    public void onDrawMouseoverTooltip(float mouseX, float mouseY) {
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
    public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
        for (WLayoutElement widget : widgets) {
            widget.draw(matrices, provider);
        }
    }

    public <W extends WSlot> W getSlot(int inventoryNumber, int slotNumber) {
        Optional<WAbstractWidget> slot = getAllWidgets().stream().filter(widget -> widget instanceof WSlot && ((WSlot) widget).inventoryNumber == inventoryNumber && ((WSlot) widget).slotNumber == slotNumber).findFirst();

        if (slot.isPresent()) {
            return (W) slot.get();
        } else {
            return null;
        }
    }

    @Override
    public void onLayoutChange() {
        if (linkedContainer != null && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BaseContainerScreen<?> screen = (BaseContainerScreen<?>) MinecraftClient.getInstance().currentScreen;

            if (screen != null) {
                screen.updateDimensions();
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getX() {
        return 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getY() {
        return 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getZ() {
        return 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }


    @Deprecated
    public boolean isBlurred() {
        return false;
    }

    @Deprecated
    public <W extends WInterface> W setBlurred(boolean isBlurred) {
        return (W) this;
    }
}
