package spinnery;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import spinnery.client.integration.SpinneryConfigurationScreen;
import spinnery.common.registry.NetworkRegistry;
import spinnery.common.registry.ThemeResourceRegistry;
import spinnery.common.registry.WidgetRegistry;
import spinnery.debug.DebugHandledScreen;
import spinnery.debug.DebugHandledScreens;
import spinnery.debug.DebugScreenHandlers;

public class SpinneryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		NetworkRegistry.initializeClient();
		WidgetRegistry.initialize();
		ThemeResourceRegistry.initialize();

		SpinneryConfigurationScreen.initialize();

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			DebugHandledScreens.initialize();
		}
	}
}
