package spinnery.common.container;

import net.minecraft.entity.player.PlayerInventory;
import spinnery.common.handler.BaseScreenHandler;

@Deprecated // Use BaseScreenHandler instead.
public class BaseContainer extends BaseScreenHandler {
	public BaseContainer(int synchronizationID, PlayerInventory playerInventory) {
		super(synchronizationID, playerInventory);
	}
}
