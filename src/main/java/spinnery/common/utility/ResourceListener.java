package spinnery.common.utility;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import spinnery.common.configuration.registry.ConfigurationRegistry;
import spinnery.common.registry.ThemeResourceRegistry;

/**
 * A basic implementation of
 * a resource listener for
 * Spinnery's resources/themes.
 */
@Environment(EnvType.CLIENT)
public class ResourceListener implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = new Identifier("spinnery", "reload_listener");

	@Override
	public void apply(ResourceManager resourceManager) {
		ThemeResourceRegistry.clear();
		ThemeResourceRegistry.load(resourceManager);

		ConfigurationRegistry.load(resourceManager);
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}
}
