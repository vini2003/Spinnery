package spinnery.registry;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import spinnery.container.client.BaseScreen;
import spinnery.container.common.TestContainer;

public class ScreenRegistry {
	public static final Identifier TEST_SCREEN = register(new Identifier("test"));

	public ScreenRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static <I extends Identifier> I register(I ID) {
		ScreenProviderRegistry.INSTANCE.registerFactory(ID,
				(syncId, identifier, player, buf) -> new BaseScreen(new LiteralText("test"), new TestContainer(syncId, buf.readBlockPos(), player.inventory), player));
		return ID;
	}
}
