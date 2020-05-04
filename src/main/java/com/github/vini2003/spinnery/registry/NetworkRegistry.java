package com.github.vini2003.spinnery.registry;

import com.github.vini2003.spinnery.packet.*;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import com.github.vini2003.spinnery.common.BaseContainer;
import com.github.vini2003.spinnery.widget.WAbstractWidget;
import com.github.vini2003.spinnery.widget.WSlot;
import com.github.vini2003.spinnery.widget.api.Action;
import com.github.vini2003.spinnery.widget.api.WNetworked;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Registers all the network-related
 * assortments Spinnery makes use of.
 */
public class NetworkRegistry {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = net.minecraftforge.fml.network.NetworkRegistry.newSimpleChannel(
			new ResourceLocation("spinnery", "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	public static SlotClickPacket createSlotClickPacket(int syncId, int slotNumber, int inventoryNumber, int button, Action action) {
		return new SlotClickPacket(syncId, slotNumber, inventoryNumber, button, action);
	}

	public static SlotDragPacket createSlotDragPacket(int syncId, int[] slotNumber, int[] inventoryNumber, Action action) {
		return new SlotDragPacket(syncId, slotNumber, inventoryNumber, action);
	}

	public static SlotUpdatePacket createSlotUpdatePacket(int syncId, int slotNumber, int inventoryNumber, ItemStack stack) {
		return new SlotUpdatePacket(syncId, slotNumber, inventoryNumber, stack);
	}

	public static WidgetCustomPacket createMouseClickPacket(WNetworked widget, double mouseX, double mouseY, int button) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WNetworked.Event.MOUSE_CLICK, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.MOUSE_CLICK, payload);
	}

	public static WidgetCustomPacket createMouseReleasePacket(WNetworked widget, double mouseX, double mouseY, int button) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WNetworked.Event.MOUSE_RELEASE, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.MOUSE_SCROLL, payload);
	}

	public static WidgetCustomPacket createMouseDragPacket(WNetworked widget, double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		payload.putDouble("deltaX", deltaX);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WNetworked.Event.MOUSE_DRAG, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.MOUSE_DRAG, payload);
	}

	public static WidgetCustomPacket createMouseScrollPacket(WNetworked widget, double mouseX, double mouseY, double deltaY) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WNetworked.Event.MOUSE_SCROLL, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.MOUSE_SCROLL, payload);
	}

	public static WidgetCustomPacket createFocusPacket(WNetworked widget, boolean focused) {
		CompoundNBT payload = new CompoundNBT();
		payload.putBoolean("focused", focused);
		widget.appendPayload(WNetworked.Event.FOCUS, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.FOCUS, payload);
	}

	public static WidgetCustomPacket createKeyPressPacket(WNetworked widget, int character, int keyCode, int keyModifier) {
		CompoundNBT payload = new CompoundNBT();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WNetworked.Event.KEY_PRESS, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.KEY_PRESS, payload);
	}

	public static WidgetCustomPacket createKeyReleasePacket(WNetworked widget, int character, int keyCode, int keyModifier) {
		CompoundNBT payload = new CompoundNBT();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WNetworked.Event.KEY_RELEASE, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.KEY_RELEASE, payload);
	}

	public static WidgetCustomPacket createCharTypePacket(WNetworked widget, char character, int keyCode) {
		CompoundNBT payload = new CompoundNBT();
		payload.putString("character", String.valueOf(character));
		payload.putInt("keyCode", keyCode);
		widget.appendPayload(WNetworked.Event.CHAR_TYPE, payload);

		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.CHAR_TYPE, payload);
	}

	public static WidgetCustomPacket createCustomInterfaceEventPacket(WNetworked widget, CompoundNBT payload) {
		return new WidgetCustomPacket(widget.getSyncId(), WNetworked.Event.CUSTOM, payload);
	}

	public static void sendCustomInterfaceEvent(WNetworked widget, CompoundNBT payload) {
		INSTANCE.sendToServer(NetworkRegistry.createCustomInterfaceEventPacket(widget, payload));
	}

	public static void initializeServer() {
		INSTANCE.registerMessage(SlotClickPacket.ID, SlotClickPacket.class, SlotClickPacket::encode, SlotClickPacket::new, SlotClickPacket::handle);
		INSTANCE.registerMessage(SlotDragPacket.ID, SlotDragPacket.class, SlotDragPacket::encode, SlotDragPacket::new, SlotDragPacket::handle);
		INSTANCE.registerMessage(WidgetSyncPacket.ID, WidgetSyncPacket.class, WidgetSyncPacket::encode, WidgetSyncPacket::new, WidgetSyncPacket::handle);
		INSTANCE.registerMessage(WidgetCustomPacket.ID, WidgetCustomPacket.class, WidgetCustomPacket::encode, WidgetCustomPacket::new, WidgetCustomPacket::handle);
	}

	public static void initializeClient() {
		INSTANCE.registerMessage(SlotUpdatePacket.ID, SlotUpdatePacket.class, SlotUpdatePacket::encode, SlotUpdatePacket::new, SlotUpdatePacket::handle);
	}
}
