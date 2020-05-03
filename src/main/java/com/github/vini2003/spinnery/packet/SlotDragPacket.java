package com.github.vini2003.spinnery.packet;

import com.github.vini2003.spinnery.common.BaseContainer;
import com.github.vini2003.spinnery.widget.api.Action;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SlotDragPacket {
	public static final int ID = 1;

	int syncId;
	int[] slotNumbers;
	int[] inventoryNumbers;
	Action action;

	public SlotDragPacket(int syncId, int[] slotNumbers, int[] inventoryNumbers, Action action) {
		this.syncId = syncId;
		this.slotNumbers = slotNumbers;
		this.inventoryNumbers = inventoryNumbers;
		this.action = action;
	}

	public SlotDragPacket(PacketBuffer buffer) {
		this.syncId = buffer.readInt();
		this.slotNumbers = buffer.readVarIntArray();
		this.inventoryNumbers = buffer.readVarIntArray();
		this.action = buffer.readEnumValue(Action.class);
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(syncId);
		buffer.writeVarIntArray(slotNumbers);
		buffer.writeVarIntArray(inventoryNumbers);
		buffer.writeEnumValue(action);
	}

	public static void handle(SlotDragPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();

			if (player.openContainer instanceof BaseContainer && player.container.windowId == packet.syncId) {
				((BaseContainer) player.openContainer).onSlotDrag(packet.slotNumbers, packet.inventoryNumbers, packet.action);
			}
		});
	}
}