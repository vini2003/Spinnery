package spinnery.debug;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseHandledScreen;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class DebugHandledScreen extends BaseHandledScreen<DebugScreenHandler> {
	public DebugHandledScreen(DebugScreenHandler handler, PlayerInventory playerInventory, Text name) {
		super(handler, playerInventory, name);

		WInterface mainInterface = getInterface();

		WPanel panel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(192, 128));

		panel.center();

		panel.createChild(WText::new, Position.of(panel, 7, 7, 0)).setText("Hi there!");
		panel.createChild(WText::new, Position.of(panel, 7, 18, 0)).setText("07/11/2020 11:01:47PM");

		WVerticalList scrollableContainer = panel.createChild(WVerticalList::new, Position.of(panel, 7, 7, 0), Size.of(64, 96));

		for (int k = 0; k < 32; ++k) {
			scrollableContainer.addRow(new WKibbyImage().setSize(Size.of(18, 18)));
		}

		WTabHolder tabHolder = panel.createChild(WTabHolder::new, Position.of(panel, 7, 30, 0), Size.of(64, 60));
		WTabHolder.WTab tabA = tabHolder.addTab(Items.APPLE);
		WTabHolder.WTab tabB = tabHolder.addTab(Items.CACTUS);
		tabA.getBody().createChild(WSlot::new, Position.of(tabA.getBody(), 7, 7, 0), Size.of(18, 18)).setInventoryNumber(0).setSlotNumber(0);
		tabB.getBody().createChild(WSlot::new, Position.of(tabA.getBody(), 7, 7, 0), Size.of(18, 18)).setInventoryNumber(0).setSlotNumber(0);
	}
}
