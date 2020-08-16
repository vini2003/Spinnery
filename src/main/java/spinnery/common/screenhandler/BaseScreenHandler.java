package spinnery.common.screenhandler;

import me.shedaniel.math.Rectangle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import spinnery.common.utilities.Networks;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;

public abstract class BaseScreenHandler extends ScreenHandler {
	protected final WInterface serverInterface;

	private final PlayerEntity player;

	private final boolean client;

	private Rectangle rectangle = new Rectangle();

	public BaseScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerEntity player) {
		super(type, syncId);
		this.player = player;
		this.client = player.world.isClient;
		serverInterface = new WInterface(this);
	}

	public WInterface getInterface() {
		return serverInterface;
	}

	public void handlePacket(Identifier id, PacketByteBuf buf) {
		int hash = buf.readInt();

		getInterface().getAllWidgets().forEach((widget) -> {
			if (widget.getHash() == hash) {
				if (Networks.MOUSE_MOVE.equals(id)) {
					widget.onMouseMoved(buf.readFloat(), buf.readFloat());
				} else if (Networks.MOUSE_CLICK.equals(id)) {
					widget.onMouseClicked(buf.readFloat(), buf.readFloat(), buf.readInt());
				} else if (Networks.MOUSE_RELEASE.equals(id)) {
					widget.onMouseReleased(buf.readFloat(), buf.readFloat(), buf.readInt());
				} else if (Networks.MOUSE_DRAG.equals(id)) {
					widget.onMouseDragged(buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readDouble(), buf.readDouble());
				} else if (Networks.MOUSE_SCROLL.equals(id)) {
					widget.onMouseScrolled(buf.readFloat(), buf.readFloat(), buf.readDouble());
				} else if (Networks.KEY_PRESS.equals(id)) {
					widget.onKeyPressed(buf.readInt(), buf.readInt(), buf.readInt());
				} else if (Networks.KEY_RELEASE.equals(id)) {
					widget.onKeyReleased(buf.readInt(), buf.readInt(), buf.readInt());
				} else if (Networks.CHAR_TYPE.equals(id)) {
					widget.onCharTyped(buf.readChar(), buf.readInt());
				} else if (Networks.FOCUS_GAIN.equals(id)) {
					widget.onFocusGained();
				} else if (Networks.FOCUS_RELEASE.equals(id)) {
					widget.onFocusReleased();
				}
			}
		});
	}

	public void onLayoutChange() {
		float minimumX = Float.MAX_VALUE;
		float minimumY = Float.MAX_VALUE;
		float maximumX = 0F;
		float maximumY = 0F;

		for (WAbstractWidget widget : getInterface().getWidgets()) {
			if (widget.getX() < minimumX) {
				minimumX = widget.getX();
			}
			if (widget.getX() + widget.getWidth() > maximumX) {
				maximumX = widget.getX() + widget.getWidth();
			}
			if (widget.getY() < minimumY) {
				minimumY = widget.getY();
			}
			if (widget.getY() + widget.getHeight() > maximumY) {
				maximumY = widget.getY() + widget.getHeight();
			}
		}

		rectangle = new Rectangle((int) minimumX, (int) minimumY, (int) (maximumX - minimumX), (int) (maximumY - minimumY));

		if (client) {
			onLayoutChangedDelegate();
		}
	}

	@Environment(EnvType.CLIENT)
	public void onLayoutChangedDelegate() {
		Screen screen = MinecraftClient.getInstance().currentScreen;

		if (screen instanceof HandledScreen) {
			HandledScreen handledScreen = (HandledScreen) screen;
			handledScreen.x = rectangle.getMinX();
			handledScreen.y = rectangle.getMinY();

			handledScreen.backgroundWidth = rectangle.getWidth();
			handledScreen.backgroundHeight = rectangle.getHeight();
		}
	}

	public abstract void initialize(int width, int height);
}
