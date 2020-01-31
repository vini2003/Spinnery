package spinnery;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spinnery.registry.ResourceRegistry;
import spinnery.registry.WidgetRegistry;

public class SpinneryClient implements ClientModInitializer {
	public static final String LOG_ID = "Spinnery";
	public static final Identifier MOD_ID = new Identifier(LOG_ID.toLowerCase());
	public static Logger LOGGER = LogManager.getLogger("Spinnery");

	@Override
	public void onInitializeClient() {
		WidgetRegistry.initialize();
		ResourceRegistry.initialize();
	}
}
