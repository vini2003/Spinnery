package spinnery.common.utilities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import spinnery.common.configuration.registry.ConfigurationRegistry;


  a resource listener for
 /
@Environment(EnvType.CLIENT)
public class Resources implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = new Identifier("spinnery", "reload_listener");

	@Override
	public void apply(ResourceManager resourceManager) {
		Themes.Resources.clear();
		Themes.Resources.load(resourceManager);
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}
}
