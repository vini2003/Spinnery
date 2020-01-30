package spinnery.registry;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import spinnery.common.BaseContainer;
import spinnery.util.StackUtilities;
import spinnery.widget.WSlot;
import spinnery.widget.WSynced;
import spinnery.widget.WWidget;

public class NetworkRegistry {
	public static final Identifier SLOT_CLICK_PACKET = new Identifier("spinnery", "slot_click");
	public static final Identifier SLOT_UPDATE_PACKET = new Identifier("spinnery", "slot_update");
	public static final Identifier SYNCED_WIDGET_PACKET = new Identifier("spinnery", "synced_widget");

	public static PacketByteBuf createSlotClickPacket(int slotNumber, int inventoryNumber, int button, SlotActionType action) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(slotNumber);
		buffer.writeInt(inventoryNumber);
		buffer.writeInt(button);
		buffer.writeEnumConstant(action);
		return buffer;
	}

	public static PacketByteBuf createSlotUpdatePacket(int slotNumber, int inventoryNumber, ItemStack stack) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(slotNumber);
		buffer.writeInt(inventoryNumber);
		buffer.writeCompoundTag(StackUtilities.write(stack));
		return buffer;
	}

	public static PacketByteBuf createMouseClickPacket(WSynced widget, double mouseX, double mouseY, int button) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.MOUSE_CLICK);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createMouseReleasePacket(WSynced widget, double mouseX, double mouseY, int button) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.MOUSE_RELEASE);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createMouseDragPacket(WSynced widget, double mouseX, double mouseY, int button,
													  double deltaX, double deltaY) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.MOUSE_DRAG);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		payload.putDouble("deltaX", deltaX);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createMouseScrollPacket(WSynced widget, double mouseX, double mouseY, double deltaY) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.MOUSE_SCROLL);
		CompoundTag payload = new CompoundTag();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createFocusPacket(WSynced widget, boolean focused) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.FOCUS);
		CompoundTag payload = new CompoundTag();
		payload.putBoolean("focused", focused);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createKeyPressPacket(WSynced widget, int character, int keyCode, int keyModifier) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.KEY_PRESS);
		CompoundTag payload = new CompoundTag();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createKeyReleasePacket(WSynced widget, int character, int keyCode, int keyModifier) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.KEY_RELEASE);
		CompoundTag payload = new CompoundTag();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static PacketByteBuf createCharTypePacket(WSynced widget, char character, int keyCode) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(widget.getSyncId());
		buffer.writeEnumConstant(WSynced.Event.CHAR_TYPE);
		CompoundTag payload = new CompoundTag();
		payload.putString("character", String.valueOf(character));
		payload.putInt("keyCode", keyCode);
		widget.appendPayload(WSynced.Event.MOUSE_CLICK, payload);
		buffer.writeCompoundTag(payload);
		return buffer;
	}

	public static void initialize() {
		// TODO: Warn or mitigate packet flooding
		ServerSidePacketRegistry.INSTANCE.register(SLOT_CLICK_PACKET, (packetContext, packetByteBuffer) -> {
			int slotNumber = packetByteBuffer.readInt();
			int inventoryNumber = packetByteBuffer.readInt();
			int button = packetByteBuffer.readInt();
			SlotActionType action = packetByteBuffer.readEnumConstant(SlotActionType.class);
			packetContext.getTaskQueue().execute(() -> {
				if (packetContext.getPlayer().container instanceof BaseContainer) {
					((BaseContainer) packetContext.getPlayer().container).onSlotClicked(slotNumber, inventoryNumber, button, action, packetContext.getPlayer());
				}
			});
		});

		ClientSidePacketRegistry.INSTANCE.register(SLOT_UPDATE_PACKET, (packetContext, packetByteBuffer) -> {
			int slotNumber = packetByteBuffer.readInt();
			int inventoryNumber = packetByteBuffer.readInt();
			CompoundTag tag = packetByteBuffer.readCompoundTag();
			ItemStack stack = StackUtilities.read(tag);

			packetContext.getTaskQueue().execute(() -> {
				if (MinecraftClient.getInstance().player.container instanceof BaseContainer) {
					BaseContainer container = (BaseContainer) MinecraftClient.getInstance().player.container;

					container.getInventory(inventoryNumber).setInvStack(slotNumber, stack);

					for (WWidget widget : container.getHolder().getAllWidgets()) {
						if (widget instanceof WSlot && ((WSlot) widget).getInventoryNumber() == inventoryNumber && ((WSlot) widget).getSlotNumber() == slotNumber) {
							((WSlot) widget).setStack(container.getInventory(inventoryNumber).getInvStack(slotNumber));
						}
					}
				}
			});


			}
		);

		ServerSidePacketRegistry.INSTANCE.register(SYNCED_WIDGET_PACKET, (packetContext, packetByteBuf) -> {
			int widgetSyncId = packetByteBuf.readInt();
			WSynced.Event event = packetByteBuf.readEnumConstant(WSynced.Event.class);
			CompoundTag payload = packetByteBuf.readCompoundTag();
			packetContext.getTaskQueue().execute(() -> {
				if (packetContext.getPlayer().container instanceof BaseContainer) {
					((BaseContainer) packetContext.getPlayer().container).onInterfaceEvent(widgetSyncId, event, payload);
				}
			});
		});
	}
}
