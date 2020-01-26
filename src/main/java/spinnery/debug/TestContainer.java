package spinnery.debug;

import net.minecraft.entity.player.PlayerInventory;
import spinnery.common.BaseContainer;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

public class TestContainer extends BaseContainer {
	public TestContainer(int synchronizationID, PlayerInventory newLinkedPlayerInventory) {
		super(synchronizationID, newLinkedPlayerInventory);

		getInventories().put(PLAYER_INVENTORY, newLinkedPlayerInventory);

		// WInterface
		WInterface mainInterface = new WInterface(this);

		getHolder().add(mainInterface);

		WSlot.addPlayerInventory(mainInterface, PLAYER_INVENTORY);
		// WInterface
	}
}