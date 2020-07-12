package spinnery.debug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseHandledScreen;
import spinnery.client.screen.BaseScreen;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class DebugHandledScreen extends BaseHandledScreen<DebugScreenHandler> {
	public DebugHandledScreen(DebugScreenHandler handler, PlayerInventory playerInventory, Text name) {
		super(handler, playerInventory, name);

		WInterface mainInterface = getInterface();

		WPanel panel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(192, 128));

		panel.center();

		panel.createChild(WStaticText::new, Position.of(panel, 7, 7, 0)).setText("Hi there!");
		panel.createChild(WStaticText::new, Position.of(panel, 7, 18, 0)).setText("07/11/2020 11:01:47PM");
	}
}
