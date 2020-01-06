package spinnery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spinnery.registry.BlockRegistry;
import spinnery.registry.ContainerRegistry;
import spinnery.registry.ItemRegistry;
import spinnery.registry.ResourceRegistry;
import spinnery.registry.ScreenRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class SpinneryMod implements ModInitializer {
	public static Logger logger = LogManager.getLogger("Spinnery");
	public static final String LOG_ID = "Spinnery";
	public static final Identifier MOD_ID = new Identifier(LOG_ID.toLowerCase());


	@Override
	public void onInitialize() {
		BlockRegistry.initialize();
		ItemRegistry.initialize();
		ScreenRegistry.initialize();
		ContainerRegistry.initialize();
		ResourceRegistry.initialize();
	}
}
