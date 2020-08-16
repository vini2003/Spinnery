package spinnery.debug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import spinnery.common.screenhandler.BaseScreenHandler;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class DebugScreenHandler extends BaseScreenHandler {
	public DebugScreenHandler(int syncId, PlayerEntity player) {
		super(DebugScreenHandlers.DEBUG, syncId, player);
	}

	@Override
	public void initialize(int width, int height) {
		WInterface mainInterface = getInterface();

		WPanel panel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(192, 128));

		panel.center();

		panel.createChild(WText::new, Position.of(panel, 7, 7)).setText("Hi there!");
		panel.createChild(WText::new, Position.of(panel, 7, 18)).setText("07/11/2020 11:01:47PM");

		WVerticalList scrollableContainer = panel.createChild(WVerticalList::new, Position.of(panel, 7, 7), Size.of(64, 96));

		for (int k = 0; k < 32; ++k) {
			scrollableContainer.addRow(new WTexture().setSize(Size.of(18, 18)));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return false;
	}
}
