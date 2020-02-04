package spinnery.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import spinnery.registry.ResourceRegistry;

@Environment(EnvType.CLIENT)
public class ResourceListener implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = new Identifier("spinnery", "reload_listener");

	@Override
	public void apply(ResourceManager resourceManager) {
		ResourceRegistry.clear();
		ResourceRegistry.load(resourceManager);
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}
}
