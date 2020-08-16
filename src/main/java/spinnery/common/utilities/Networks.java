package spinnery.common.utilities;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import spinnery.Spinnery;
import spinnery.common.screenhandler.BaseScreenHandler;

public class Networks {
	public static final Identifier INITIALIZE = Spinnery.identifier("initialize");

	public static final Identifier WIDGET_UPDATE = Spinnery.identifier("update");

	public static final Identifier MOUSE_MOVE = Spinnery.identifier("mouse_move");

	public static final Identifier MOUSE_CLICK = Spinnery.identifier("mouse_click");

	public static final Identifier MOUSE_RELEASE = Spinnery.identifier("mouse_release");

	public static final Identifier MOUSE_DRAG = Spinnery.identifier("mouse_drag");

	public static final Identifier MOUSE_SCROLL = Spinnery.identifier("mouse_scroll");

	public static final Identifier KEY_PRESS = Spinnery.identifier("key_press");

	public static final Identifier KEY_RELEASE = Spinnery.identifier("key_release");

	public static final Identifier CHAR_TYPE = Spinnery.identifier("char_type");

	public static final Identifier FOCUS_GAIN = Spinnery.identifier("focus_gain");

	public static final Identifier FOCUS_RELEASE = Spinnery.identifier("focus_release");

	public static void initialize() {
		ServerSidePacketRegistry.INSTANCE.register(WIDGET_UPDATE, (context, buf) -> {
			int syncId = buf.readInt();
			Identifier id = buf.readIdentifier();

			buf.retain();

			context.getTaskQueue().execute(() -> {
				context.getPlayer().getServer().getPlayerManager().getPlayerList().forEach((player) -> {
					if (player.currentScreenHandler.syncId == syncId && player.currentScreenHandler instanceof BaseScreenHandler) {
						((BaseScreenHandler) player.currentScreenHandler).handlePacket(id, buf);
					}
				});
			});
		});

		ServerSidePacketRegistry.INSTANCE.register(INITIALIZE, (context, buf) -> {
			int syncId = buf.readInt();

			buf.retain();

			context.getTaskQueue().execute(() -> {
				context.getPlayer().getServer().getPlayerManager().getPlayerList().forEach((player) -> {
					if (player.currentScreenHandler.syncId == syncId && player.currentScreenHandler instanceof BaseScreenHandler) {
						BaseScreenHandler handler = ((BaseScreenHandler) player.currentScreenHandler);

						handler.slots.clear();
						handler.getInterface().getWidgets().clear();
						handler.initialize(buf.readInt(), buf.readInt());
					}
				});
			});
		});
	}

	public static void toServer(Identifier id, PacketByteBuf buf) {
		ClientSidePacketRegistry.INSTANCE.sendToServer(id, buf);
	}

	public static PacketByteBuf ofInitialize(int syncId, int width, int height) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeInt(width);
		buf.writeInt(height);
		return buf;
	}

	public static PacketByteBuf ofMouseMove(int syncId, int hash, float x, float y) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(MOUSE_MOVE);
		buf.writeInt(hash);
		buf.writeFloat(x);
		buf.writeFloat(y);
		return buf;
	}

	public static PacketByteBuf ofMouseClick(int syncId, int hash, float x, float y, int button) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(MOUSE_CLICK);
		buf.writeInt(hash);
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeInt(button);
		return buf;
	}

	public static PacketByteBuf ofMouseRelease(int syncId, int hash, float x, float y, int button) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(MOUSE_RELEASE);
		buf.writeInt(hash);
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeInt(button);
		return buf;
	}

	public static PacketByteBuf ofMouseDrag(int syncId, int hash, float x, float y, int button, double deltaX, double deltaY) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(MOUSE_DRAG);
		buf.writeInt(hash);
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeInt(button);
		buf.writeDouble(deltaX);
		buf.writeDouble(deltaY);
		return buf;
	}

	public static PacketByteBuf ofMouseScroll(int syncId, int hash, float x, float y, double deltaY) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(MOUSE_SCROLL);
		buf.writeInt(hash);
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeDouble(deltaY);
		return buf;
	}

	public static PacketByteBuf ofKeyPress(int syncId, int hash, int keyCode, int scanCode, int keyModifiers) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(KEY_PRESS);
		buf.writeInt(hash);
		buf.writeInt(keyCode);
		buf.writeInt(scanCode);
		buf.writeInt(keyModifiers);
		return buf;
	}

	public static PacketByteBuf ofKeyRelease(int syncId, int hash, int keyCode, int scanCode, int keyModifiers) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(KEY_RELEASE);
		buf.writeInt(hash);
		buf.writeInt(keyCode);
		buf.writeInt(scanCode);
		buf.writeInt(keyModifiers);
		return buf;
	}

	public static PacketByteBuf ofCharType(int syncId, int hash, char character, int keyCode) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(CHAR_TYPE);
		buf.writeInt(hash);
		buf.writeChar(character);
		buf.writeInt(keyCode);
		return buf;
	}

	public static PacketByteBuf ofFocusGain(int syncId, int hash) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(FOCUS_GAIN);
		buf.writeInt(hash);
		return buf;
	}

	public static PacketByteBuf ofFocusRelease(int syncId, int hash) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		buf.writeIdentifier(FOCUS_RELEASE);
		buf.writeInt(hash);
		return buf;
	}
}
