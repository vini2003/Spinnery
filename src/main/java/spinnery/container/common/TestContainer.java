package spinnery.container.common;

import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import spinnery.block.TestBlock;
import spinnery.container.common.widget.WAnchor;
import spinnery.container.common.widget.WButton;
import spinnery.container.common.widget.WDropdown;
import spinnery.container.common.widget.WDynamicText;
import spinnery.container.common.widget.WList;
import spinnery.container.common.widget.WHorizontalSlider;
import spinnery.container.common.widget.WSlot;
import spinnery.container.common.widget.WPanel;
import net.minecraft.entity.player.PlayerInventory;
import spinnery.container.common.widget.WStaticText;
import spinnery.container.common.widget.WToggle;
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

		WDropdown dropdownA = new WDropdown(WAnchor.MC_ORIGIN, -30, 0, 0, 26, 12, 26, 76, linkedWPanel);

		WList listA = new WList(WAnchor.MC_ORIGIN, 174, 0, 0, 66, 170, linkedWPanel);


		for (int i = 0; i < 54; ++i) {
			WSlot slotA = new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, ++i, linkedInventory, linkedWPanel);
			WSlot slotB = new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, ++i, linkedInventory, linkedWPanel);
			WSlot slotC = new WSlot(WAnchor.MC_ORIGIN, 0, 0, 3, 18, 18, ++i, linkedInventory, linkedWPanel);

			listA.add(slotA, slotB, slotC);
		}

		WVerticalSlider sliderB = new WVerticalSlider(WAnchor.MC_ORIGIN, 80, 24, 0, 18, 72, 38, linkedWPanel);

		WHorizontalSlider sliderA = new WHorizontalSlider(WAnchor.MC_ORIGIN, 30, 25, 0, 36, 18, 40, linkedWPanel);

		WToggle toggleA = new WToggle(WAnchor.MC_ORIGIN, 30, 70, 0, 18, 9, linkedWPanel);
		WToggle toggleB = new WToggle(WAnchor.MC_ORIGIN, 30, 90, 0, 36, 9, linkedWPanel);
		WToggle toggleC = new WToggle(WAnchor.MC_ORIGIN, 30, 110, 0, 54, 9, linkedWPanel);

		WButton buttonA = new WButton(WAnchor.MC_ORIGIN, 90, 110, 20, 18, 18, linkedWPanel);

		WStaticText textA = new WStaticText(WAnchor.MC_ORIGIN, 110, 90, 20, 40, new LiteralText("Hey there, Cringewalker"), linkedWPanel);

		WDynamicText textB = new WDynamicText(WAnchor.MC_ORIGIN, 300, 90, 0, 100, 18, linkedWPanel);

		getLinkedPanel().add(listA, sliderA, sliderB, toggleA, toggleB, toggleC, dropdownA, buttonA, textA, textB);
	}
}
