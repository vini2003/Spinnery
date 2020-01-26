package spinnery.debug;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.util.Identifier;

public class ContainerRegistry {
	public static final Identifier TEST_CONTAINER = register(new Identifier("test"));

	public ContainerRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static <I extends Identifier> I register(I ID) {
		ContainerProviderRegistry.INSTANCE.registerFactory(ID, (syncId, id, player, buffer) -> new 	TestContainer(syncId, player.inventory));
		return ID;
	}
}