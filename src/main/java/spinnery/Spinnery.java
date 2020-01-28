package spinnery;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spinnery.debug.BlockRegistry;
import spinnery.debug.ContainerRegistry;
import spinnery.debug.ItemRegistry;
import spinnery.registry.NetworkRegistry;

public class Spinnery implements ModInitializer {
	public static final String LOG_ID = "Spinnery";
	public static final String MOD_ID = LOG_ID.toLowerCase();
	public static Logger logger = LogManager.getLogger("Spinnery");

	@Override
	public void onInitialize() {
		ItemRegistry.initialize();
		BlockRegistry.initialize();
		ContainerRegistry.initialize();
		NetworkRegistry.initialize();
	}
}
