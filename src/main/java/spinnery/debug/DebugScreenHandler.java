package spinnery.debug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import spinnery.common.screenhandler.BaseScreenHandler;
import spinnery.widget.*;
import spinnery.common.utilities.miscellaneous.Position;
import spinnery.common.utilities.miscellaneous.Size;

public class DebugScreenHandler extends BaseScreenHandler {
	public DebugScreenHandler(int syncId, PlayerEntity player) {
		super(DebugScreenHandlers.DEBUG, syncId, player);
	}

	@Override
	public void initialize(int width, int height) {
		WInterface mainInterface = getInterface();

		WPanel panel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(192, 128));

		WText textA = panel.createChild(WText::new, Position.of(panel, 7, 7)).setText("Hi there!");
		WText textB = panel.createChild(WText::new, Position.of(panel, 7, 18)).setText("07/11/2020 11:01:47PM");

		WSlot slot = panel.createChild(() -> new WSlot(new Slot(getPlayer().inventory, 0, 0, 0)), Position.of(textB, 0, 9), Size.of(18, 18));


		//WVerticalList scrollableContainer = panel.createChild(WVerticalList::new, Position.of(panel, 7, 7), Size.of(64, 96));
//
		//for (int k = 0; k < 32; ++k) {
		//	scrollableContainer.addRow(new WTexture().setSize(Size.of(18, 18)));
		//}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
