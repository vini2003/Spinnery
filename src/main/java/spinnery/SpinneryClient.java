package spinnery;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import spinnery.client.integration.SpinneryConfigurationScreen;
import spinnery.common.utilities.Networks;
import spinnery.common.utilities.Widgets;
import spinnery.debug.DebugHandledScreens;

public class SpinneryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Networks.initializeClient();
		Widgets.initialize();
		ThemeResources.initialize();

		SpinneryConfigurationScreen.initialize();

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			DebugHandledScreens.initialize();
		}
	}
}
