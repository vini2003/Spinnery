package spinnery.debug;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import spinnery.common.screenhandler.BaseScreenHandler;
import spinnery.widget.WSlot;

public class DebugScreenHandler extends BaseScreenHandler {
	public DebugScreenHandler(int synchronizationID, PlayerInventory playerInventory) {
		super(synchronizationID, playerInventory);

		getInterface().createChild(WSlot::new).setInventoryNumber(0).setSlotNumber(0);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return DebugScreenHandlers.DEBUG;
	}
}
