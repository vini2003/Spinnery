package spinnery;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spinnery.debug.ScreenRegistry;
import spinnery.registry.ResourceRegistry;

public class SpinneryClient implements ClientModInitializer {
	public static final String LOG_ID = "Spinnery";
	public static final Identifier MOD_ID = new Identifier(LOG_ID.toLowerCase());
	public static Logger logger = LogManager.getLogger("Spinnery");

	@Override
	public void onInitializeClient() {
		ScreenRegistry.initialize();
		ResourceRegistry.initialize();
	}
}
