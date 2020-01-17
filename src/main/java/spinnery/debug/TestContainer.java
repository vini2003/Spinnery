package spinnery.debug;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.BaseContainer;
import spinnery.widget.WAnchor;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;

public class TestContainer extends BaseContainer {
	public static void init() {

	}
	public TestContainer(int synchronizationID, BlockPos blockPos, PlayerInventory newLinkedPlayerInventory) {
		super(synchronizationID, newLinkedPlayerInventory);

		setLinkedPanel(new WPanel(0, 0, 0, 200, 200, this));
		setLinkedInventory(newLinkedPlayerInventory);
		getLinkedPanel().center();

		BasicInventory inv = new BasicInventory(1);

		WSlot meinSlot = new WSlot(WAnchor.MC_ORIGIN, 8, 8, 0, 18, 18, 0, inv, linkedPanel);

		linkedPanel.add(meinSlot);

		WSlot.addPlayerInventory(0, 18, 18, linkedPlayerInventory, linkedPanel);
	}
}