package spinnery.registry;

import spinnery.container.client.BaseScreen;
import spinnery.container.common.BaseContainer;
import spinnery.container.common.TestContainer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ScreenRegistry {
	public ScreenRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static final Identifier TEST_SCREEN = register(new Identifier("test"));

	public static <I extends  Identifier> I register(I ID) {
		ScreenProviderRegistry.INSTANCE.registerFactory(ID,
				(syncId, identifier, player, buf) -> new BaseScreen(new LiteralText("test"), new TestContainer(syncId, buf.readBlockPos(), player.inventory), player));
		return ID;
	}
}
