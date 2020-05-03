package com.github.vini2003.spinnery.packet;

import com.github.vini2003.spinnery.common.BaseContainer;
import com.github.vini2003.spinnery.widget.api.WNetworked;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WidgetSyncPacket {
	public static final int ID = 3;

	int widgetSyncId;
	WNetworked.Event event;
	CompoundNBT payload;

	public WidgetSyncPacket(int widgetSyncId, WNetworked.Event event, CompoundNBT payload) {
		this.widgetSyncId = widgetSyncId;
		this.event = event;
		this.payload = payload;
	}

	public WidgetSyncPacket(PacketBuffer buffer) {
		this.widgetSyncId = buffer.readInt();
		this.event = buffer.readEnumValue(WNetworked.Event.class);
		this.payload = buffer.readCompoundTag();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(widgetSyncId);
		buffer.writeEnumValue(event);
		buffer.writeCompoundTag(payload);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();

			if (player != null && player.openContainer instanceof BaseContainer) {
				((BaseContainer) player.openContainer).onInterfaceEvent(widgetSyncId, event, payload);
			}
		});
	}

	public static WidgetSyncPacket createMouseClickPacket(WNetworked widget, double mouseX, double mouseY, int button) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WNetworked.Event.MOUSE_CLICK, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.MOUSE_CLICK, payload);
	}

	public static WidgetSyncPacket createMouseReleasePacket(WNetworked widget, double mouseX, double mouseY, int button) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		widget.appendPayload(WNetworked.Event.MOUSE_RELEASE, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.MOUSE_RELEASE, payload);
	}

	public static WidgetSyncPacket createMouseDragPacket(WNetworked widget, double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putInt("button", button);
		payload.putDouble("deltaX", deltaX);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WNetworked.Event.MOUSE_DRAG, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.MOUSE_DRAG, payload);
	}

	public static WidgetSyncPacket createMouseScrollPacket(WNetworked widget, double mouseX, double mouseY, double deltaY) {
		CompoundNBT payload = new CompoundNBT();
		payload.putDouble("mouseX", mouseX);
		payload.putDouble("mouseY", mouseY);
		payload.putDouble("deltaY", deltaY);
		widget.appendPayload(WNetworked.Event.MOUSE_SCROLL, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.MOUSE_SCROLL, payload);
	}

	public static WidgetSyncPacket createFocusPacket(WNetworked widget, boolean focused) {
		CompoundNBT payload = new CompoundNBT();
		payload.putBoolean("focused", focused);
		widget.appendPayload(WNetworked.Event.FOCUS, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.FOCUS, payload);
	}

	public static WidgetSyncPacket createKeyPressPacket(WNetworked widget, int character, int keyCode, int keyModifier) {
		CompoundNBT payload = new CompoundNBT();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WNetworked.Event.KEY_PRESS, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.KEY_PRESS, payload);
	}

	public static WidgetSyncPacket createKeyReleasePacket(WNetworked widget, int character, int keyCode, int keyModifier) {
		CompoundNBT payload = new CompoundNBT();
		payload.putInt("character", character);
		payload.putInt("keyCode", keyCode);
		payload.putInt("keyModifier", keyModifier);
		widget.appendPayload(WNetworked.Event.KEY_RELEASE, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.KEY_RELEASE, payload);
	}

	public static WidgetSyncPacket createCharTypePacket(WNetworked widget, char character, int keyCode) {
		CompoundNBT payload = new CompoundNBT();
		payload.putString("character", String.valueOf(character));
		payload.putInt("keyCode", keyCode);
		widget.appendPayload(WNetworked.Event.CHAR_TYPE, payload);

		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.CHAR_TYPE, payload);
	}

	public static WidgetSyncPacket createCustomInterfaceEventPacket(WNetworked widget, CompoundNBT payload) {
		return new WidgetSyncPacket(widget.getSyncId(), WNetworked.Event.CUSTOM, payload);
	}
}