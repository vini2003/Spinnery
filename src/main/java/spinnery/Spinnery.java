package spinnery;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spinnery.registry.NetworkRegistry;
import spinnery.registry.WidgetRegistry;

public class Spinnery implements ModInitializer {
	public static final String LOG_ID = "Spinnery";
	public static final String MOD_ID = LOG_ID.toLowerCase();
	public static Logger logger = LogManager.getLogger("Spinnery");

	@Override
	public void onInitialize() {
		NetworkRegistry.initialize();
		WidgetRegistry.initialize();
	}
}
