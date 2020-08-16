package spinnery.debug;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseHandledScreen;

public class DebugHandledScreen extends BaseHandledScreen<DebugScreenHandler> {
	public DebugHandledScreen(DebugScreenHandler handler, PlayerInventory playerInventory, Text name) {
		super(handler, playerInventory, name);


	}
}
