package spinnery.debug;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import spinnery.common.handler.BaseScreenHandler;

public class DebugScreenHandler extends BaseScreenHandler {
	public DebugScreenHandler(int synchronizationID, PlayerInventory playerInventory) {
		super(synchronizationID, playerInventory);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return DebugScreenHandlers.DEBUG;
	}
}
