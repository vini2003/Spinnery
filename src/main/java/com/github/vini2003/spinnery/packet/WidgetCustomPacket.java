package com.github.vini2003.spinnery.packet;

import com.github.vini2003.spinnery.common.BaseContainer;
import com.github.vini2003.spinnery.widget.api.Action;
import com.github.vini2003.spinnery.widget.api.WNetworked;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WidgetCustomPacket {
	public static final int ID = 2;

	public int syncId;
	public WNetworked.Event event;
	public CompoundNBT payload;

	public WidgetCustomPacket(int syncId, WNetworked.Event event, CompoundNBT payload) {
		this.syncId = syncId;
		this.event = event;
		this.payload = payload;
	}

	public WidgetCustomPacket(PacketBuffer buffer) {
		this.syncId = buffer.readInt();
		this.event = buffer.readEnumValue(WNetworked.Event.class);
		this.payload = buffer.readCompoundTag();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(syncId);
		buffer.writeEnumValue(event);
		buffer.writeCompoundTag(payload);
	}

	public static void handle(WidgetCustomPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (context.get().getSender().openContainer instanceof BaseContainer) {
				((BaseContainer) context.get().getSender().openContainer).onInterfaceEvent(packet.syncId, packet.event, packet.payload);
			}
		});
	}
}