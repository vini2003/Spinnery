package spinnery;

import net.fabricmc.api.ClientModInitializer;
import spinnery.debug.ScreenRegistry;
import spinnery.registry.NetworkRegistry;
import spinnery.registry.ResourceRegistry;
import spinnery.registry.WidgetRegistry;

public class SpinneryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		NetworkRegistry.initializeClient();
		WidgetRegistry.initialize();
		ResourceRegistry.initialize();
		ScreenRegistry.initialize();
	}
}
