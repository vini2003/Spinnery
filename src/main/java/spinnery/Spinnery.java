package spinnery;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spinnery.common.configuration.registry.ConfigurationRegistry;
import spinnery.common.registry.NetworkRegistry;
import spinnery.debug.BlockRegistry;
import spinnery.debug.ContainerRegistry;
import spinnery.debug.ItemRegistry;
import spinnery.debug.ScreenRegistry;

public class Spinnery implements ModInitializer {
	public static final String LOG_ID = "Spinnery";
	public static final String MOD_ID = "spinnery";
	public static Logger LOGGER = LogManager.getLogger("Spinnery");

	@Override
	public void onInitialize() {
		ItemRegistry.initialize();
		BlockRegistry.initialize();
		ContainerRegistry.initialize();
		ScreenRegistry.initialize();
		NetworkRegistry.initialize();
		ConfigurationRegistry.initialize();
	}
}
