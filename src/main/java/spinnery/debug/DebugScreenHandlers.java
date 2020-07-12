package spinnery.debug;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class DebugScreenHandlers {
	public static void initialize() {
	}

	public static final ScreenHandlerType<DebugScreenHandler> DEBUG = ScreenHandlerRegistry.registerExtended(new Identifier("spinnery", "debug"), ((syncId, playerInventory, packetByteBuf) -> {
		return new DebugScreenHandler(syncId, playerInventory);
	}));
}
