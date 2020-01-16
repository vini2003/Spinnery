package spinnery.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import spinnery.registry.ResourceRegistry;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ResourceListener implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = new Identifier("spinnery", "reload_listener");

	@Override
	public void apply(ResourceManager resourceManager) {
		try {
			ResourceRegistry.reload(resourceManager.getResource(new Identifier("spinnery", "default.json")).getInputStream());
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}
}
