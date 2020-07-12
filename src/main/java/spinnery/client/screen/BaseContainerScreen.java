package spinnery.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.common.container.BaseContainer;
import spinnery.common.handler.BaseScreenHandler;

@Deprecated // Use BaseHandledScreen instead.
public class BaseContainerScreen<T extends BaseContainer> extends BaseHandledScreen<T> {
	public BaseContainerScreen(Text name, T handler, PlayerEntity player) {
		super(name, handler, player);
	}
}
