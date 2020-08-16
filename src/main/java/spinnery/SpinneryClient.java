package spinnery;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import spinnery.common.utilities.Networks;
import spinnery.common.utilities.Themes;
import spinnery.common.utilities.Widgets;
import spinnery.debug.DebugHandledScreens;

public class SpinneryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Networks.initialize();
		Widgets.initialize();
		Themes.Resources.initialize();

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			DebugHandledScreens.initialize();
		}
	}
}
