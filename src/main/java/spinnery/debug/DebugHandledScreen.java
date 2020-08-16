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


	}
}
