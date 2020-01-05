package spinnery.container.common;

import net.minecraft.util.math.BlockPos;
import spinnery.block.TestBlock;
import spinnery.container.common.widget.WAnchor;
import spinnery.container.common.widget.WSlot;
import spinnery.container.common.widget.WPanel;
import net.minecraft.entity.player.PlayerInventory;


// maybe an addWidgets which takes a vararg widgets?


public class TestContainer extends BaseContainer {
	public TestContainer(int synchronizationID, BlockPos newLinkedInventoryPosition, PlayerInventory newLinkedPlayerInventory) {
		super(synchronizationID, newLinkedPlayerInventory);

		this.linkedInventory = ((TestBlock) newLinkedPlayerInventory.player.world.getBlockState(newLinkedInventoryPosition).getBlock()).getInventory();

		setLinkedPanel(new WPanel(0, 0, -10, 170, 178 + 32, this));

		WSlot.addPlayerInventory(0, 18, 18, linkedPlayerInventory, linkedWPanel);

		WSlot.addArray(WAnchor.PANEL_TOP_LEFT, 6, 6, 31, 10, 0, 18, 18, 0, linkedInventory, linkedWPanel);

		System.out.println(linkedInventory);
	}
}
