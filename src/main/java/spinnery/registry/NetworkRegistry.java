package spinnery.registry;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.container.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import spinnery.common.BaseContainer;

public class NetworkRegistry {
	public static final Identifier SLOT_CLICK_PACKET = new Identifier("spinnery", "slot_click");

	public static PacketByteBuf createSlotClickPacket(int slotNumber, int inventoryNumber, int button, SlotActionType action) {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeInt(slotNumber);
		buffer.writeInt(inventoryNumber);
		buffer.writeInt(button);
		buffer.writeEnumConstant(action);
		return buffer;
	}

	public static void initialize() {
		ServerSidePacketRegistry.INSTANCE.register(SLOT_CLICK_PACKET, (packetContext, packetByteBuffer) -> {
			int slotNumber = packetByteBuffer.readInt();
			int inventoryNumber = packetByteBuffer.readInt();
			int button = packetByteBuffer.readInt();
			SlotActionType action = packetByteBuffer.readEnumConstant(SlotActionType.class);
			packetContext.getTaskQueue().execute(() -> {
				((BaseContainer) packetContext.getPlayer().container).onSlotClicked(slotNumber, inventoryNumber, button, action, packetContext.getPlayer());
			});
		});
	}
}
