package spinnery;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spinnery.common.utilities.Networks;
import spinnery.debug.DebugCommands;
import spinnery.debug.DebugScreenHandlers;

public class Spinnery implements ModInitializer {
	public static final String LOG_ID = "Spinnery";
	public static final String MOD_ID = "spinnery";
	public static Logger LOGGER = LogManager.getLogger(LOG_ID);

	public static final EnvType ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		Networks.initialize();

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			DebugScreenHandlers.initialize();
			DebugCommands.initialize();
		}
	}
}
