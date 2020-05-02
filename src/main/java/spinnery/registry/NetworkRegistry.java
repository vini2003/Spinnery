package spinnery.registry;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import spinnery.common.BaseContainer;
import spinnery.util.StackUtilities;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WSlot;
import spinnery.widget.api.Action;
import spinnery.widget.api.WNetworked;

/**
 * Registers all the network-related
 * assortments Spinnery makes use of.
 */
public class NetworkRegistry {
	public static final Identifier SLOT_CLICK_PACKET = new Identifier("spinnery", "slot_click");
	public static final Identifier SLOT_UPDATE_PACKET = new Identifier("spinnery", "slot_update");
	public static final Identifier SLOT_DRAG_PACKET = new Identifier("spinnery", "slot_drag");
	public static final Identifier SYNCED_WIDGET_PACKET = new Identifier("spinnery", "synced_widget");

	public static PacketByteBuf createSlotClickPacket(int syncId, int slotNumber, int inventoryNumber, int button, Action action) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(syncId);
		buffer.writeInt(slotNumber);
		buffer.writeInt(inventoryNumber);
		buffer.writeInt(button);
		buffer.writeEnumConstant(action);
		return buffer;
	}

	public static PacketByteBuf createSlotDragPacket(int syncId, int[] slotNumber, int[] inventoryNumber, Action action) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(syncId);
		buffer.writeIntArray(slotNumber);
		buffer.writeIntArray(inventoryNumber);
		buffer.writeEnumConstant(action);
		return buffer;
	}

	public static PacketByteBuf createSlotUpdatePacket(int syncId, int slotNumber, int inventoryNumber, ItemStack stack) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(syncId);
		buffer.writeInt(slotNumber);
		buffer.writeInt(inventoryNumber);
		buffer.writeCompoundTag(stack.toTag(new CompoundTag()));
		return buffer;
	}

	public static PacketByteBuf createMouseClickPacket(WNetworked widget, double mouseX, double mouseY, int button) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.MOUSE_CLICK);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WNetworked.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createMouseReleasePacket(WNetworked widget, double mouseX, double mouseY, int button) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.MOUSE_RELEASE);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WNetworked.Event.MOUSE_RELEASE, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createMouseDragPacket(WNetworked widget, double mouseX, double mouseY, int button,
													  double deltaX, double deltaY) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.MOUSE_DRAG);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		payload.putDouble("deltaX", deltaX);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WNetworked.Event.MOUSE_DRAG, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createMouseScrollPacket(WNetworked widget, double mouseX, double mouseY, double deltaY) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.MOUSE_SCROLL);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WNetworked.Event.MOUSE_SCROLL, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createFocusPacket(WNetworked widget, boolean focused) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.FOCUS);
		CompoundTag payload = new CompoundTag();
		payload.putBoolean("focused", focused);
		widget.appendPayload(WNetworked.Event.FOCUS, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createKeyPressPacket(WNetworked widget, int character, int keyCode, int keyModifier) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.KEY_PRESS);
		CompoundTag payload = new CompoundTag();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WNetworked.Event.KEY_PRESS, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createKeyReleasePacket(WNetworked widget, int character, int keyCode, int keyModifier) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.KEY_RELEASE);
		CompoundTag payload = new CompoundTag();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WNetworked.Event.KEY_RELEASE, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createCharTypePacket(WNetworked widget, char character, int keyCode) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.CHAR_TYPE);
		CompoundTag payload = new CompoundTag();
		payload.putString("character", String.valueOf(character));
		payload.putInt("keyCode", keyCode);
		widget.appendPayload(WNetworked.Event.CHAR_TYPE, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static void sendCustomInterfaceEvent(WNetworked widget, CompoundTag payload) {
		ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SYNCED_WIDGET_PACKET,
				NetworkRegistry.createCustomInterfaceEventPacket(widget, payload));
	}

	public static PacketByteBuf createCustomInterfaceEventPacket(WNetworked widget, CompoundTag payload) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WNetworked.Event.CUSTOM);
		widget.appendPayload(WNetworked.Event.CUSTOM, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static void initialize() {
		// TODO: Warn or mitigate packet flooding
		ServerSidePacketRegistry.INSTANCE.register(SLOT_CLICK_PACKET, (packetContext, packetByteBuffer) -> {
			int syncId = packetByteBuffer.readInt();
			int slotNumber = packetByteBuffer.readInt();
			int inventoryNumber = packetByteBuffer.readInt();
			int button = packetByteBuffer.readInt();
			Action action = packetByteBuffer.readEnumConstant(Action.class);

			packetContext.getTaskQueue().execute(() -> {
				if (packetContext.getPlayer().container instanceof BaseContainer && packetContext.getPlayer().container.syncId == syncId) {
					((BaseContainer) packetContext.getPlayer().container).onSlotAction(slotNumber, inventoryNumber, button, action, packetContext.getPlayer());
				}
			});
		});

		ServerSidePacketRegistry.INSTANCE.register(SLOT_DRAG_PACKET, ((packetContext, packetByteBuffer) -> {
			int syncId = packetByteBuffer.readInt();
			int[] slotNumbers = packetByteBuffer.readIntArray();
			int[] inventoryNumbers = packetByteBuffer.readIntArray();
			Action action = packetByteBuffer.readEnumConstant(Action.class);

			packetContext.getTaskQueue().execute(() -> {
				if (packetContext.getPlayer().container instanceof BaseContainer && packetContext.getPlayer().container.syncId == syncId) {
					((BaseContainer) packetContext.getPlayer().container).onSlotDrag(slotNumbers, inventoryNumbers, action);
				}
			});
		}));

		ServerSidePacketRegistry.INSTANCE.register(SYNCED_WIDGET_PACKET, (packetContext, packetByteBuf) -> {
			int widgetSyncId = packetByteBuf.readInt();
			WNetworked.Event event = packetByteBuf.readEnumConstant(WNetworked.Event.class);
			CompoundTag payload = packetByteBuf.readCompoundTag();
			packetContext.getTaskQueue().execute(() -> {
				if (packetContext.getPlayer().container instanceof BaseContainer) {
					((BaseContainer) packetContext.getPlayer().container).onInterfaceEvent(widgetSyncId, event, payload);
				}
			});
		});
	}

	@Environment(EnvType.CLIENT)
	public static void initializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(SLOT_UPDATE_PACKET, (packetContext, packetByteBuffer) -> {
					int syncId = packetByteBuffer.readInt();
					int slotNumber = packetByteBuffer.readInt();
					int inventoryNumber = packetByteBuffer.readInt();
					CompoundTag tag = packetByteBuffer.readCompoundTag();
					ItemStack stack = ItemStack.fromTag(tag);

					packetContext.getTaskQueue().execute(() -> {
						if (packetContext.getPlayer().container instanceof BaseContainer && packetContext.getPlayer().container.syncId == syncId) {
							BaseContainer container = (BaseContainer) packetContext.getPlayer().container;

							container.getInventory(inventoryNumber).setInvStack(slotNumber, stack);

							for (WAbstractWidget widget : container.getInterface().getAllWidgets()) {
								if (widget instanceof WSlot && ((WSlot) widget).getInventoryNumber() == inventoryNumber && ((WSlot) widget).getSlotNumber() == slotNumber) {
									((WSlot) widget).setStack(container.getInventory(inventoryNumber).getInvStack(slotNumber));
								}
							}
						}
					});
				}
		);
	}
}
