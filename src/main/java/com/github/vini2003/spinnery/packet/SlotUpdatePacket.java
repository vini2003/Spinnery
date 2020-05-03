package com.github.vini2003.spinnery.packet;

import com.github.vini2003.spinnery.common.BaseContainer;
import com.github.vini2003.spinnery.widget.WAbstractWidget;
import com.github.vini2003.spinnery.widget.WSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SlotUpdatePacket {
	public static final int ID =3;

	int syncId;
	int slotNumber;
	int inventoryNumber;
	ItemStack stack;

	public SlotUpdatePacket(int syncId, int slotNumber, int inventoryNumber, ItemStack stack) {
		this.syncId = syncId;
		this.slotNumber = slotNumber;
		this.inventoryNumber = inventoryNumber;
		this.stack = stack;
	}

	public SlotUpdatePacket(PacketBuffer buffer) {
		this.syncId = buffer.readInt();
		this.slotNumber = buffer.readInt();
		this.inventoryNumber = buffer.readInt();
		this.stack = ItemStack.read(buffer.readCompoundTag());
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(syncId);
		buffer.writeInt(slotNumber);
		buffer.writeInt(inventoryNumber);
		buffer.writeCompoundTag(stack.write(new CompoundNBT()));
	}

	public static void handle(SlotUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();

			if (player.openContainer instanceof BaseContainer && player.openContainer.windowId == packet.syncId) {
				BaseContainer container = (BaseContainer) player.openContainer;

				container.getInventory(packet.inventoryNumber).setInventorySlotContents(packet.slotNumber, packet.stack);

				for (WAbstractWidget widget : container.getInterface().getAllWidgets()) {
					if (widget instanceof WSlot && ((WSlot) widget).getInventoryNumber() == packet.inventoryNumber && ((WSlot) widget).getSlotNumber() == packet.slotNumber) {
						((WSlot) widget).setStack(container.getInventory(packet.inventoryNumber).getStackInSlot(packet.slotNumber));
					}
				}
			}
		});
	}
}