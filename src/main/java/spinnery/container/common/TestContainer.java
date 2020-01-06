package spinnery.container.common;

import net.minecraft.util.math.BlockPos;
import spinnery.block.TestBlock;
import spinnery.container.common.widget.WAnchor;
import spinnery.container.common.widget.WDropdown;
import spinnery.container.common.widget.WList;
import spinnery.container.common.widget.WHorizontalSlider;
import spinnery.container.common.widget.WSlot;
import spinnery.container.common.widget.WPanel;
import net.minecraft.entity.player.PlayerInventory;
import spinnery.container.common.widget.WVerticalSlider;


// maybe an addWidgets which takes a vararg widgets?


public class TestContainer extends BaseContainer {
	public TestContainer(int synchronizationID, BlockPos newLinkedInventoryPosition, PlayerInventory newLinkedPlayerInventory) {
		super(synchronizationID, newLinkedPlayerInventory);

		setLinkedInventory(((TestBlock) newLinkedPlayerInventory.player.world.getBlockState(newLinkedInventoryPosition).getBlock()).getInventory());

		setLinkedPanel(new WPanel(30, 0, -10, 170, 210, this));

		WSlot.addPlayerInventory(0, 18, 18, linkedPlayerInventory, linkedWPanel);

		//WSlot.addArray(WAnchor.MC_ORIGIN, 6, 6, 31, 10, 0, 18, 18, 0, linkedInventory, linkedWPanel);

		//getLinkedPanel().add(new WToggle(WAnchor.MC_ORIGIN, 8, 24, 0, 18, 9, linkedWPanel));

		WDropdown dropdown = new WDropdown(WAnchor.MC_ORIGIN, -30, 0, 0, 26, 12, 26, 76, linkedWPanel);

		dropdown.add(new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, 1, linkedInventory, linkedWPanel));
		dropdown.add(new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, 2, linkedInventory, linkedWPanel));
		dropdown.add(new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, 3, linkedInventory, linkedWPanel));

		WList list = new WList(WAnchor.MC_ORIGIN, 174, 0, 0, 66, 76, linkedWPanel);

		for (int i = 0; i < 27; ++i) {
			list.add(new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, ++i, linkedInventory, linkedWPanel), new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, ++i, linkedInventory, linkedWPanel), new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, ++i, linkedInventory, linkedWPanel));
		}

		WVerticalSlider slider = new WVerticalSlider(WAnchor.MC_ORIGIN, 240, 8, 0, 18, 72, 38, linkedWPanel);

		WHorizontalSlider horizontalSlider = new WHorizontalSlider(WAnchor.MC_ORIGIN, 220, 8, 0, 72, 18, 38, linkedWPanel);

		getLinkedPanel().add(list);
	}
}
